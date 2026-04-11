package com.evandhardspace.chat.data.network

import com.evandhardspace.chat.domain.model.ConnectionState

expect class ConnectionErrorHandler {
    fun getConnectionStateForError(cause: Throwable): ConnectionState
    fun mapCancellationToNetworkException(exception: Throwable): Throwable
    fun isRetriable(cause: Throwable): Boolean
}
