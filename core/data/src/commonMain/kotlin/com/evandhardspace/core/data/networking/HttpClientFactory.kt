package com.evandhardspace.core.data.networking

import com.evandhardspace.core.data.BuildKonfig
import com.evandhardspace.core.data.dto.AuthInfoDto
import com.evandhardspace.core.data.dto.request.RefreshRequest
import com.evandhardspace.core.data.mapper.toDomain
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.logging.ChatAppLogger
import com.evandhardspace.core.domain.util.fold
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.auth.Auth
import io.ktor.client.plugins.auth.providers.BearerTokens
import io.ktor.client.plugins.auth.providers.bearer
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.request.header
import io.ktor.client.statement.request
import io.ktor.http.ContentType
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.json.Json

class HttpClientFactory(
    private val appLogger: ChatAppLogger,
    private val sessionRepository: MutableSessionRepository,
    private val json: Json,
) {
    fun create(engine: HttpClientEngine): HttpClient = HttpClient(engine) {
        install(ContentNegotiation) {
            json(
                json = json,
            )
        }
        install(HttpTimeout) {
            socketTimeoutMillis = 20_000L
            requestTimeoutMillis = 20_000L
        }
        install(Logging) {
            logger = object : Logger {
                override fun log(message: String) {
                    appLogger.debug(message)
                }
            }
            level = LogLevel.ALL
        }
        install(WebSockets) {
            pingIntervalMillis = 20_000L
        }
        defaultRequest {
            header("x-api-key", BuildKonfig.API_KEY)
            contentType(ContentType.Application.Json)
        }

        install(Auth) {
            bearer {
                loadTokens {
                    (sessionRepository.authState.first() as? AuthState.Authenticated)?.let {
                        BearerTokens(
                            accessToken = it.accessToken,
                            refreshToken = it.refreshToken,
                        )
                    }
                }
                refreshTokens {
                    if ("auth/" in response.request.url.encodedPath) return@refreshTokens null

                    val authState: AuthState? = sessionRepository.authState.firstOrNull()

                    if (authState == null || authState !is AuthState.Authenticated) {
                        sessionRepository.logout()
                        return@refreshTokens null
                    }

                    client.post<RefreshRequest, AuthInfoDto>(
                        route = "/auth/refresh", // TODO(8),
                        body = RefreshRequest(
                            refreshToken = authState.refreshToken,
                        ),
                    ) { markAsRefreshTokenRequest() }.fold(
                        onSuccess = { newAuthInfo ->
                            val savedAuthInfo = sessionRepository.saveAuthInfo(newAuthInfo.toDomain())
                            BearerTokens(
                                savedAuthInfo.accessToken,
                                savedAuthInfo.refreshToken,
                            )
                        },
                        onFailure = {
                            sessionRepository.logout()
                            null
                        },
                    )
                }
            }
        }
    }
}
