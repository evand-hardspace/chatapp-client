package com.evandhardspace.core.data.networking

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.ErrorEither
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
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.raise
import com.evandhardspace.core.domain.util.bind
import com.evandhardspace.core.domain.util.errorResult
import com.evandhardspace.core.domain.util.either
import kotlinx.coroutines.currentCoroutineContext

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Either<DataError.Remote, T>
): Either<DataError.Remote, T> = either {
    try {
        handleResponse(execute()).bind()
    } catch (e: DarwinHttpRequestException) {
        handleDarwinException(e).bind()
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

private fun handleDarwinException(e: DarwinHttpRequestException): ErrorEither<DataError.Remote> =
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
