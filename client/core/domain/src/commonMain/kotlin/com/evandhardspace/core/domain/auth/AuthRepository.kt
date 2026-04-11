package com.evandhardspace.core.domain.auth

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.Either

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String,
    ): Either<DataError.Remote, AuthState.Authenticated>

    suspend fun register(
        email: String,
        username: String,
        password: String,
    ): EmptyEither<DataError.Remote>

    suspend fun resendVerificationEmail(
        email: String,
    ): EmptyEither<DataError.Remote>

    suspend fun verifyEmail(
        token: String,
    ): EmptyEither<DataError.Remote>

    suspend fun forgotPassword(email: String): EmptyEither<DataError.Remote>

    suspend fun resetPassword(
        newPassword: String,
        token: String,
    ): EmptyEither<DataError.Remote>

    suspend fun changePassword(
        currentPassword: String,
        newPassword: String,
    ): EmptyEither<DataError.Remote>

    suspend fun logout(refreshToken: String): EmptyEither<DataError.Remote>
}
