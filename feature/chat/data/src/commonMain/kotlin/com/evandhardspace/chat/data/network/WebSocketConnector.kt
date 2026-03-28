package com.evandhardspace.chat.data.network

import com.evandhardspace.chat.data.dto.websocket.WebSocketMessageDto
import com.evandhardspace.chat.data.lifecycle.AppLifecycleObserver
import com.evandhardspace.chat.domain.error.ConnectionError
import com.evandhardspace.chat.domain.model.ConnectionState
import com.evandhardspace.core.data.networking.UrlConstants
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.logging.ChatAppLogger
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.asSuccess
import com.evandhardspace.core.domain.util.either
import com.evandhardspace.core.domain.util.raise
import com.evandhardspace.feature.chat.data.BuildKonfig
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.header
import io.ktor.websocket.Frame
import io.ktor.websocket.WebSocketSession
import io.ktor.websocket.close
import io.ktor.websocket.readText
import io.ktor.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.NonCancellable
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import kotlin.time.Duration.Companion.seconds

private const val DefaultConnectionDebounceSeconds = 1

@Single
internal class WebSocketConnector(
    private val httpClient: HttpClient,
    private val applicationScope: CoroutineScope,
    private val sessionStorage: SessionRepository,
    private val json: Json,
    private val connectionErrorHandler: ConnectionErrorHandler,
    private val connectionRetryHandler: ConnectionRetryHandler,
    private val appLifecycleObserver: AppLifecycleObserver,
    private val connectivityObserver: ConnectivityObserver,
    private val logger: ChatAppLogger,
) {
    val connectionState: StateFlow<ConnectionState>
        field = MutableStateFlow(ConnectionState.Disconnected)

    private var currentSession: WebSocketSession? = null

    private val isConnected = connectivityObserver
        .isConnected
        .debounce(DefaultConnectionDebounceSeconds.seconds)
        .stateIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = false,
        )

    private val isInForeground: StateFlow<Boolean> = appLifecycleObserver
        .isForeground
        .onEach { isInForeground ->
            if (isInForeground) {
                connectionRetryHandler.resetDelay()
            }
        }
        .stateIn(
            applicationScope,
            SharingStarted.WhileSubscribed(5000),
            false,
        )

    val messages: Flow<WebSocketMessageDto> = combine(
        sessionStorage.authState,
        isConnected,
        isInForeground
    ) { authState, isConnected, isInForeground ->
        when {
            authState !is AuthState.Authenticated -> {
                logger.info("No authentication details. Clearing session and disconnecting...")
                connectionState.value = ConnectionState.Disconnected
                currentSession?.close()
                currentSession = null
                connectionRetryHandler.resetDelay()
                null
            }

            isInForeground.not() -> {
                logger.info("App in background, disconnecting socket proactively.")
                connectionState.value = ConnectionState.Disconnected
                currentSession?.close()
                currentSession = null
                null
            }

            isConnected.not() -> {
                logger.info("Device is disconnected, closing WebSocket connection.")
                connectionState.value = ConnectionState.ErrorNetwork
                currentSession?.close()
                currentSession = null
                authState.accessToken
            }

            else -> {
                logger.info("App in foreground & connected. Establishing connection...")

                if (connectionState.value !in listOf(
                        ConnectionState.Connecting,
                        ConnectionState.Connected,
                    )
                ) {
                    connectionState.value = ConnectionState.Connecting
                }

                authState.accessToken
            }
        }
    }.flatMapLatest { accessToken: String? ->
        if (accessToken == null) return@flatMapLatest emptyFlow()

        createWebSocketFlow(accessToken)
            // Catch block to transform exceptions for platform compatibility
            .catch { e ->
                logger.error("Exception in WebSocket", e)
                currentSession?.close()
                currentSession = null
                throw connectionErrorHandler.mapCancellationToNetworkException(e)
            }
            .retryWhen { t, attempt ->
                logger.info("Connection failed on attempt $attempt")

                val shouldRetry = connectionRetryHandler.shouldRetry(t, attempt.toInt())

                if (shouldRetry) {
                    connectionState.value = ConnectionState.Connecting
                    connectionRetryHandler.applyRetryDelay(attempt.toInt())
                }
                shouldRetry
            }
            // Catch block for non-retriable errors
            .catch { e ->
                logger.error("Unhandled WebSocket error", e)

                connectionState.value = connectionErrorHandler.getConnectionStateForError(e)
            }
    }

    suspend fun sendMessage(message: String): EmptyEither<ConnectionError> = either {
        val connectionState = connectionState.value

        if (currentSession == null || connectionState != ConnectionState.Connected) {
            ConnectionError.NotConnected.raise()
        }

        return try {
            currentSession?.send(message)
            Unit.asSuccess()
        } catch (e: Exception) {
            currentCoroutineContext().ensureActive()
            logger.error("Unable to send WebSocket message", e)
            ConnectionError.MessageSendFailed.raise()
        }
    }

    private fun createWebSocketFlow(accessToken: String): Flow<WebSocketMessageDto> = callbackFlow {
        connectionState.value = ConnectionState.Connecting

        currentSession = httpClient.webSocketSession(
            urlString = "${UrlConstants.BaseUrlWS}/chat"
        ) {
            header("Authorization", "Bearer $accessToken")
            header("X-API-Key", BuildKonfig.API_KEY)
        }

        currentSession?.let { session ->
            connectionState.value = ConnectionState.Connected

            session
                .incoming
                .consumeAsFlow()
                .buffer(
                    capacity = 100,
                )
                .collect { frame ->
                    when (frame) {
                        is Frame.Text -> {
                            val text = frame.readText()
                            logger.info("Received raw text frame: $text")

                            val messageDto = json.decodeFromString<WebSocketMessageDto>(text)
                            send(messageDto)
                        }

                        is Frame.Ping -> {
                            logger.debug("Received ping from server. Sending pong...")
                            session.send(Frame.Pong(frame.data))
                        }

                        else -> Unit
                    }
                }
        } ?: throw Exception("Failed to establish WebSocket connection")

        awaitClose {
            launch {
                withContext(NonCancellable) {
                    logger.info("Disconnecting from WebSocket session...")
                    connectionState.value = ConnectionState.Disconnected
                    currentSession?.close()
                    currentSession = null
                }
            }
        }
    }
}
