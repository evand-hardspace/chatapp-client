package com.evandhardspace.chat.data.network

import kotlinx.coroutines.delay
import org.koin.core.annotation.Single
import kotlin.math.pow

@Single
class ConnectionRetryHandler(
    private val connectionErrorHandler: ConnectionErrorHandler,
) {
    private var shouldSkipBackoff = false

    fun shouldRetry(cause: Throwable, attempt: Int): Boolean =
        connectionErrorHandler.isRetriable(cause)

    suspend fun applyRetryDelay(attempt: Int) {
        if(shouldSkipBackoff.not()) {
            val delay = createBackoffDelay(attempt)
            delay(delay)
        } else {
            shouldSkipBackoff = false
        }
    }

    fun resetDelay() {
        shouldSkipBackoff = true
    }

    private fun createBackoffDelay(attempt: Int): Long {
        val delayTime = (2f.pow(attempt) * MinDelayMs).toLong()
        return minOf(delayTime, MaxDelayMs)
    }
}
private const val MinDelayMs = 2_000L
private const val MaxDelayMs = 30_000L