package com.evandhardspace.chat.data.network

import com.evandhardspace.chat.domain.model.ConnectionState
import kotlinx.coroutines.CancellationException
import platform.Foundation.NSError
import platform.Foundation.NSURLErrorDomain
import platform.Foundation.NSURLErrorNetworkConnectionLost
import platform.Foundation.NSURLErrorNotConnectedToInternet
import platform.Foundation.NSURLErrorTimedOut

actual class ConnectionErrorHandler {
    actual fun getConnectionStateForError(cause: Throwable): ConnectionState {
        val nsError = extractNsError(cause)

        return when {
            nsError != null -> {
                when (nsError.code) {
                    NSURLErrorNotConnectedToInternet,
                    NSURLErrorNetworkConnectionLost,
                    NSURLErrorTimedOut -> ConnectionState.ErrorNetwork

                    else -> ConnectionState.ErrorUnknown
                }
            }
            cause is IosNetworkCancellationException -> {
                ConnectionState.ErrorNetwork
            }
            else -> ConnectionState.ErrorUnknown
        }
    }

    actual fun mapCancellationToNetworkException(exception: Throwable): Throwable {
        if(exception is CancellationException) {
            val cause = exception.cause ?: return exception
            val isDarwinException = cause.message?.contains("DarwinHttpRequestException") == true
            val isConnectionLostException = cause.message?.contains("NSURLErrorDomain Code=-1005") == true
            val isNotConnectedException = cause.message?.contains("NSURLErrorDomain Code=-1009") == true

            if(isDarwinException || isConnectionLostException || isNotConnectedException) {
                return IosNetworkCancellationException(
                    message = "Network connection lost (extracted from cancellation)",
                    cause = cause,
                )
            }
        }

        return exception
    }

    actual fun isRetriable(cause: Throwable): Boolean {
        if(cause is IosNetworkCancellationException) return true

        return when(extractNsError(cause)?.code) {
            NSURLErrorNotConnectedToInternet,
            NSURLErrorNetworkConnectionLost,
            NSURLErrorTimedOut -> true
            else -> false
        }
    }

    private fun extractNsError(cause: Throwable): NSError? {
        val throwableCause = cause.cause
        if (throwableCause is NSError) return throwableCause
        if (cause is NSError) return cause

        val exceptionNsError = cause.toNSError()
        val causeNsError = cause.cause?.toNSError()

        return exceptionNsError ?: causeNsError
    }

    private fun Throwable.toNSError(): NSError? {
        return message?.let { message ->
            when {
                message.contains(NSURLErrorNotConnectedToInternetPattern) ->
                    return NSError.errorWithDomain(
                        domain = NSURLErrorDomain,
                        code = NSURLErrorNotConnectedToInternet,
                        userInfo = null
                    )
                message.contains(NSURLErrorNetworkConnectionLostPattern) ->
                    return NSError.errorWithDomain(
                        domain = NSURLErrorDomain,
                        code = NSURLErrorNetworkConnectionLost,
                        userInfo = null
                    )
                else -> null
            }
        }
    }

    companion object {
        private val NSURLErrorNotConnectedToInternetPattern =
            "Error Domain=${NSURLErrorDomain} Code=${NSURLErrorNotConnectedToInternet}"
        val NSURLErrorNetworkConnectionLostPattern =
            "Error Domain=${NSURLErrorDomain} Code=${NSURLErrorNetworkConnectionLost}"
    }
}