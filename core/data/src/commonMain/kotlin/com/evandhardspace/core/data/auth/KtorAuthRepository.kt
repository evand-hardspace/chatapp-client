package com.evandhardspace.core.data.auth

import com.evandhardspace.core.data.dto.AuthInfoDto
import com.evandhardspace.core.data.dto.request.ChangePasswordRequest
import com.evandhardspace.core.data.dto.request.EmailRequest
import com.evandhardspace.core.data.dto.request.LoginRequest
import com.evandhardspace.core.data.dto.request.RefreshRequest
import com.evandhardspace.core.data.dto.request.RegisterRequest
import com.evandhardspace.core.data.dto.request.ResetPasswordRequest
import com.evandhardspace.core.data.mapper.toDomain
import com.evandhardspace.core.data.networking.get
import com.evandhardspace.core.data.networking.post
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.map
import com.evandhardspace.core.domain.util.onSuccess
import io.ktor.client.HttpClient
import io.ktor.client.plugins.auth.authProvider
import io.ktor.client.plugins.auth.providers.BearerAuthProvider

internal class KtorAuthRepository(
    private val httpClient: HttpClient,
) : AuthRepository {
    override suspend fun register(
        email: String,
        username: String,
        password: String,
    ): EmptyEither<DataError.Remote> = httpClient.post(
        route = "/auth/register",
        body = RegisterRequest(
            email = email,
            username = username,
            password = password,
        ),
    )

    override suspend fun resendVerificationEmail(
        email: String,
    ): EmptyEither<DataError.Remote> = httpClient.post(
        route = "/auth/resend-verification",
        body = EmailRequest(email),
    )

    override suspend fun verifyEmail(
        token: String,
    ): EmptyEither<DataError.Remote> = httpClient.get(
        route = "/auth/verify",
        queryParams = mapOf(
            "token" to token,
        ),
    )

    override suspend fun forgotPassword(email: String): EmptyEither<DataError.Remote> {
        return httpClient.post<EmailRequest, Unit>(
            route = "/auth/forgot-password",
            body = EmailRequest(email),
        )
    }

    override suspend fun login(
        email: String,
        password: String,
    ): Either<DataError.Remote, AuthState.Authenticated> =
        httpClient.post<LoginRequest, AuthInfoDto>(
            route = "/auth/login",
            body = LoginRequest(
                email = email,
                password = password,
            ),
        ).map(AuthInfoDto::toDomain)

    override suspend fun resetPassword(
        newPassword: String,
        token: String
    ): EmptyEither<DataError.Remote> = httpClient.post(
        route = "/auth/reset-password",
        body = ResetPasswordRequest(
            newPassword = newPassword,
            token = token,
        )
    )

    override suspend fun changePassword(
        currentPassword: String,
        newPassword: String
    ): EmptyEither<DataError.Remote> = httpClient.post(
        route = "/auth/change-password",
        body = ChangePasswordRequest(
            oldPassword = currentPassword,
            newPassword = newPassword,
        )
    )

    override suspend fun logout(refreshToken: String): EmptyEither<DataError.Remote> =
        httpClient.post<RefreshRequest, Unit>(
            route = "/auth/logout",
            body = RefreshRequest(refreshToken),
        ).onSuccess {
            httpClient.authProvider<BearerAuthProvider>()?.clearToken()
        }
}
