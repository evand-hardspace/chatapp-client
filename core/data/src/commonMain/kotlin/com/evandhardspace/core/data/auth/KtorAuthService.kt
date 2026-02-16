package com.evandhardspace.core.data.auth

import com.evandhardspace.core.data.auth.dto.request.EmailRequest
import com.evandhardspace.core.data.auth.dto.request.RegisterRequest
import com.evandhardspace.core.data.networking.post
import com.evandhardspace.core.domain.auth.AuthService
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyResult
import io.ktor.client.HttpClient

internal class KtorAuthService(
    private val client: HttpClient,
) : AuthService {
    override suspend fun register(
        email: String,
        username: String,
        password: String,
    ): EmptyResult<DataError.Remote> = client.post(
        route = "/auth/register",
        body = RegisterRequest(
            email = email,
            username = username,
            password = password,
        ),
    )

    override suspend fun resendVerificationEmail(email: String): EmptyResult<DataError.Remote> {
        return client.post(
            route = "/auth/resend-verification",
            body = EmailRequest(email),
        )
    }
}
