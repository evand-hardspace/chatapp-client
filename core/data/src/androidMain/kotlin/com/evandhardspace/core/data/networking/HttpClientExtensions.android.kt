package com.evandhardspace.core.data.networking

import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Result
import com.evandhardspace.core.domain.util.ensure
import com.evandhardspace.core.domain.util.raise
import com.evandhardspace.core.domain.util.result
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
    handleResponse: suspend (HttpResponse) -> Result<DataError.Remote, T>
): Result<DataError.Remote, T> = result {
    try {
        handleResponse(execute()).ensure()
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
