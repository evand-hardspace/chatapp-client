package com.evandhardspace.core.data.auth

import com.evandhardspace.core.data.dto.AuthInfoDto
import com.evandhardspace.core.data.dto.request.EmailRequest
import com.evandhardspace.core.data.dto.request.LoginRequest
import com.evandhardspace.core.data.dto.request.RegisterRequest
import com.evandhardspace.core.data.dto.request.ResetPasswordRequest
import com.evandhardspace.core.data.mapper.toDomain
import com.evandhardspace.core.data.networking.get
import com.evandhardspace.core.data.networking.post
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyResult
import com.evandhardspace.core.domain.util.Result
import com.evandhardspace.core.domain.util.map
import io.ktor.client.HttpClient

internal class KtorAuthRepository(
    private val client: HttpClient,
) : AuthRepository {
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

    override suspend fun resendVerificationEmail(
        email: String,
    ): EmptyResult<DataError.Remote> = client.post(
        route = "/auth/resend-verification",
        body = EmailRequest(email),
    )

    override suspend fun verifyEmail(
        token: String,
    ): EmptyResult<DataError.Remote> = client.get(
        route = "/auth/verify",
        queryParams = mapOf(
            "token" to token,
        ),
    )

    override suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote> {
        return client.post<EmailRequest, Unit>(
            route = "/auth/forgot-password",
            body = EmailRequest(email),
        )
    }

    override suspend fun login(
        email: String,
        password: String,
    ): Result<DataError.Remote, AuthState.Authenticated> = client.post<LoginRequest, AuthInfoDto>(
        route = "/auth/login",
        body = LoginRequest(
            email = email,
            password = password,
        ),
    ).map(AuthInfoDto::toDomain)

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyResult<DataError.Remote> = client.post(
        route = "/auth/reset-password",
        body = ResetPasswordRequest(
            newPassword = newPassword,
            token = token,
        )
    )
}
