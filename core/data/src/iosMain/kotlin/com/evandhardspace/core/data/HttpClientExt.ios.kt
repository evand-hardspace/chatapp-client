package com.evandhardspace.core.data

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.ErrorResult
import io.ktor.client.engine.darwin.DarwinHttpRequestException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import platform.Foundation.NSURLErrorCallIsActive
import platform.Foundation.NSURLErrorCannotFindHost
import platform.Foundation.NSURLErrorDNSLookupFailed
import platform.Foundation.NSURLErrorDataNotAllowed
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorInternationalRoamingOff
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorResourceUnavailable
import platform.Foundation.NSURLErrorTimedOut
import com.evandhardspace.core.domain.util.Result
import com.evandhardspace.core.domain.util.ensure
import com.evandhardspace.core.domain.util.errorResult
import com.evandhardspace.core.domain.util.raise
import com.evandhardspace.core.domain.util.result
import kotlinx.coroutines.currentCoroutineContext

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Result<DataError.Remote, T>
): Result<DataError.Remote, T> = result {
    try {
        handleResponse(execute()).ensure()
    } catch (e: DarwinHttpRequestException) {
        handleDarwinException(e).ensure()
    } catch (_: UnresolvedAddressException) {
        DataError.Remote.NoInternet.raise()
    } catch (_: HttpRequestTimeoutException) {
        DataError.Remote.RequestTimeout.raise()
    } catch (_: SerializationException) {
        DataError.Remote.Serialization.raise()
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        DataError.Remote.Unknown.raise()
    }
}

private fun handleDarwinException(e: DarwinHttpRequestException): ErrorResult<DataError.Remote> =
    errorResult {
        val nsError = e.origin

        if (nsError.domain != NSURLErrorDomain) DataError.Remote.Unknown.raise()

        when (nsError.code) {
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost,
            NSURLErrorCannotFindHost,
            NSURLErrorDNSLookupFailed,
            NSURLErrorResourceUnavailable,
            NSURLErrorInternationalRoamingOff,
            NSURLErrorCallIsActive,
            NSURLErrorDataNotAllowed -> DataError.Remote.NoInternet.raise()

            NSURLErrorTimedOut -> DataError.Remote.RequestTimeout.raise()
            else -> DataError.Remote.Unknown.raise()
        }
    }
