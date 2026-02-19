package com.evandhardspace.core.domain.auth

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyResult
import com.evandhardspace.core.domain.util.Result

interface AuthRepository {

    suspend fun login(
        email: String,
        password: String,
    ): Result<DataError.Remote, AuthState.Authorized>

    suspend fun register(
        email: String,
        username: String,
        password: String,
    ): EmptyResult<DataError.Remote>

    suspend fun resendVerificationEmail(
        email: String,
    ): EmptyResult<DataError.Remote>

    suspend fun verifyEmail(
        token: String,
    ): EmptyResult<DataError.Remote>

    suspend fun forgotPassword(email: String): EmptyResult<DataError.Remote>

    suspend fun resetPassword(
        newPassword: String,
        token: String,
    ): EmptyResult<DataError.Remote>
}