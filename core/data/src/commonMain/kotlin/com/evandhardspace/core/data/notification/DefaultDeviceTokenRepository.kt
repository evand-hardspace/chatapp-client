package com.evandhardspace.core.data.notification

import com.evandhardspace.core.data.dto.request.RegisterDeviceTokenRequest
import com.evandhardspace.core.data.networking.delete
import com.evandhardspace.core.data.networking.post
import com.evandhardspace.core.domain.notification.DeviceTokenRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither
import io.ktor.client.HttpClient

class DefaultDeviceTokenRepository(
    private val httpClient: HttpClient,
) : DeviceTokenRepository {

    override suspend fun registerToken(
        token: String,
        platform: String
    ): EmptyEither<DataError.Remote> =
        httpClient.post(
            route = "/notification/register",
            body = RegisterDeviceTokenRequest(
                token = token,
                platform = platform,
            )
        )

    override suspend fun unregisterToken(token: String): EmptyEither<DataError.Remote> =
        httpClient.delete(
            route = "/notification/$token",
        )
}
