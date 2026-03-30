package com.evandhardspace.chat.data.network

import com.evandhardspace.chat.domain.model.ConnectionState
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.websocket.WebSocketException
import io.ktor.network.sockets.SocketTimeoutException
import kotlinx.io.EOFException
import org.koin.core.annotation.Single
import java.net.SocketException
import java.net.UnknownHostException
import javax.net.ssl.SSLException

@Single
actual class ConnectionErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState = when (cause) {
        is ClientRequestException,
        is WebSocketException,
        is SocketException,
        is SocketTimeoutException,
        is UnknownHostException,
        is SSLException,
        is EOFException -> ConnectionState.ErrorNetwork

        else -> ConnectionState.ErrorUnknown
    }

    actual fun mapCancellationToNetworkException(exception: Throwable): Throwable = exception


    actual fun isRetriable(cause: Throwable): Boolean = when (cause) {
        is SocketTimeoutException,
        is WebSocketException,
        is SocketException,
        is EOFException -> true

        else -> false
    }
}