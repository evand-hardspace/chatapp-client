package com.evandhardspace.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

context(vm: ViewModel)
fun <T> Flow<T>.stateInUi(
    initialValue: T,
): StateFlow<T> = stateIn(
    scope = vm.viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000L),
    initialValue = initialValue,
)

/**
 * Performs the given [action] only once when the [Flow] starts, based on the state of [initOwner].
 *
 * This is useful for triggering initial data loading or one-time setup logic when a UI
 * component starts observing the flow, ensuring the action isn't repeated on configuration
 * changes or re-subscriptions if the [flowInitOwner] persists.
 *
 * @param flowInitOwner The owner that tracks whether the initial action has already been performed.
 * @param action The block of code to execute the first time the flow is started.
 * @return A [Flow] that triggers [action] on its first collection.
 */
fun <T> Flow<T>.onInit(
    flowInitOwner: FlowInitOwner,
    action: () -> Unit
): Flow<T> = onStart {
    require(flowInitOwner is FlowInitOwnerImpl) { "FlowInitOwner must be FlowInitOwnerImpl" }

    if (flowInitOwner.hasLoadedInitialData.not()) {
        action()
        flowInitOwner.hasLoadedInitialData = true
    }
}

sealed interface FlowInitOwner

fun FlowInitOwner(): FlowInitOwner = FlowInitOwnerImpl()

internal class FlowInitOwnerImpl : FlowInitOwner {
    var hasLoadedInitialData = false
}
