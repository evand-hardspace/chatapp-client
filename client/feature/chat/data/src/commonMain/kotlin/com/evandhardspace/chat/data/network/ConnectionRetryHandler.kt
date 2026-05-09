package com.evandhardspace.chat.data.network

import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.delay
import kotlin.math.pow

@SingleIn(AppScope::class)
@Inject
class ConnectionRetryHandler(
    private val connectionErrorHandler: ConnectionErrorHandler,
) {
    private var shouldSkipBackoff = false

    fun shouldRetry(cause: Throwable, attempt: Int): Boolean =
        connectionErrorHandler.isRetriable(cause)

    suspend fun applyRetryDelay(attempt: Int) {
        if (shouldSkipBackoff.not()) {
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
