package com.evandhardspace.core.domain.notification

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither

interface DeviceTokenRepository {

    suspend fun registerToken(
        token: String,
        platform: String,
    ): EmptyEither<DataError.Remote>

    suspend fun unregisterToken(
        token: String,
    ): EmptyEither<DataError.Remote>
}
