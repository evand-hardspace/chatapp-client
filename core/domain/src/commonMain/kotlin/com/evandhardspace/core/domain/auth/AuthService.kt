package com.evandhardspace.core.domain.auth

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyResult

interface AuthService {
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
}