package com.evandhardspace.core.domain.auth

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyResult

interface AuthService {
    suspend fun register(
        email: String,
        username: String,
        password: String,
    ): EmptyResult<DataError.Remote>

}