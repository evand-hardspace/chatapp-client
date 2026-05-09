package com.evandhardspace.chat.data.lifecycle

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.ProcessLifecycleOwner
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flowOn

@SingleIn(AppScope::class)
@Inject
actual class AppLifecycleObserver {
    actual val isForeground: Flow<Boolean>
        get() = callbackFlow {
            val lifecycle = ProcessLifecycleOwner.get().lifecycle

            val isAtLeastStarted = lifecycle.currentState.isAtLeast(Lifecycle.State.STARTED)
            send(isAtLeastStarted)

            val observer = LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> trySend(true)
                    Lifecycle.Event.ON_STOP -> trySend(false)
                    else -> Unit
                }
            }

            lifecycle.addObserver(observer)

            awaitClose {
                lifecycle.removeObserver(observer)
            }
        }.flowOn(Dispatchers.Main)
            .distinctUntilChanged()
}
