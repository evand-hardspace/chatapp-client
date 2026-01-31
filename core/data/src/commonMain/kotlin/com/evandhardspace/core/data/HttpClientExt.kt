package com.evandhardspace.core.data

import com.evandhardspace.core.domain.util.DataError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import com.evandhardspace.core.domain.util.Result
import com.evandhardspace.core.domain.util.raise
import com.evandhardspace.core.domain.util.result

expect suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<DataError.Remote, T>
): Result<DataError.Remote, T>

suspend inline fun <reified T> safeCall(
    noinline execute: suspend () -> HttpResponse
): Result<DataError.Remote, T> = platformSafeCall(execute = execute) { response ->
    responseToResult(response)
}

suspend inline fun <reified T> responseToResult(response: HttpResponse): Result<DataError.Remote, T> =
    result {
        when (response.status.value) {
            in 200..299 -> {
                try {
                    response.body<T>()
                } catch (_: NoTransformationFoundException) {
                    DataError.Remote.Serialization.raise()
                }
            }

            400 -> DataError.Remote.BadRequest.raise()
            401 -> DataError.Remote.Unauthorized.raise()
            403 -> DataError.Remote.Forbidden.raise()
            404 -> DataError.Remote.NotFound.raise()
            408 -> DataError.Remote.RequestTimeout.raise()
            413 -> DataError.Remote.PayloadTooLarge.raise()
            429 -> DataError.Remote.TooManyRequests.raise()
            500 -> DataError.Remote.ServerError.raise()
            503 -> DataError.Remote.ServiceUnavailable.raise()
            else -> DataError.Remote.Unknown.raise()
        }
    }

fun String.ensureBaseUrlRoute(): String = when {
    this.contains(UrlConstants.BaseUrlHttp) -> this
    this.startsWith("/") -> "${UrlConstants.BaseUrlHttp}$this"
    else -> "${UrlConstants.BaseUrlHttp}/$this"
}

