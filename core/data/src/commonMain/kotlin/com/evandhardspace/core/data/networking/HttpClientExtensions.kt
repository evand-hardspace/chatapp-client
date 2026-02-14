package com.evandhardspace.core.data.networking

import com.evandhardspace.core.domain.util.DataError
import io.ktor.client.call.NoTransformationFoundException
import io.ktor.client.call.body
import io.ktor.client.statement.HttpResponse
import com.evandhardspace.core.domain.util.Result
import com.evandhardspace.core.domain.util.raise
import com.evandhardspace.core.domain.util.result
import io.ktor.client.HttpClient
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.delete
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url

expect suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<DataError.Remote, T>
): Result<DataError.Remote, T>

suspend inline fun <reified T> safeCall(
    noinline execute: suspend () -> HttpResponse
): Result<DataError.Remote, T> = platformSafeCall(execute = execute) { response ->
    responseToResult(response)
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.post(
    route: String,
    body: Request,
    queryParams: Map<String, Any> = mapOf(),
    crossinline builder: HttpRequestBuilder.() -> Unit = {},
): Result<DataError.Remote, Response> {
    return safeCall {
        post {
            url(route.ensureBaseUrlRoute())
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
            builder()
        }
    }
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.get(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    body: Request,
    crossinline builder: HttpRequestBuilder.() -> Unit = {},
): Result<DataError.Remote, Response> {
    return safeCall {
        get {
            url(route.ensureBaseUrlRoute())
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
            builder()
        }
    }
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.delete(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    body: Request,
    crossinline builder: HttpRequestBuilder.() -> Unit = {},
): Result<DataError.Remote, Response> {
    return safeCall {
        delete {
            url(route.ensureBaseUrlRoute())
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
            builder()
        }
    }
}

suspend inline fun <reified Request, reified Response: Any> HttpClient.put(
    route: String,
    queryParams: Map<String, Any> = mapOf(),
    body: Request,
    crossinline builder: HttpRequestBuilder.() -> Unit = {},
): Result<DataError.Remote, Response> {
    return safeCall {
        put {
            url(route.ensureBaseUrlRoute())
            queryParams.forEach { (key, value) ->
                parameter(key, value)
            }
            setBody(body)
            builder()
        }
    }
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
            409 -> DataError.Remote.Conflict.raise()
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

