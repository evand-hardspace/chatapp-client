package com.evandhardspace.core.data.networking

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.raise
import com.evandhardspace.core.domain.util.bind
import com.evandhardspace.core.domain.util.either
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.statement.HttpResponse
import io.ktor.util.network.UnresolvedAddressException
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

actual suspend fun <T> platformSafeCall(
    execute: suspend () -> HttpResponse,
    handleResponse: suspend (HttpResponse) -> Either<DataError.Remote, T>
): Either<DataError.Remote, T> = either {
    try {
        handleResponse(execute()).bind()
    } catch (_: UnknownHostException) {
        DataError.Remote.NoInternet.raise()
    } catch (_: UnresolvedAddressException) {
        DataError.Remote.NoInternet.raise()
    } catch (_: ConnectException) {
        DataError.Remote.NoInternet.raise()
    } catch (_: SocketTimeoutException) {
        DataError.Remote.RequestTimeout.raise()
    } catch (_: HttpRequestTimeoutException) {
        DataError.Remote.RequestTimeout.raise()
    } catch (_: SerializationException) {
        DataError.Remote.Serialization.raise()
    } catch (_: Exception) {
        currentCoroutineContext().ensureActive()
        DataError.Remote.Unknown.raise()
    }
}
