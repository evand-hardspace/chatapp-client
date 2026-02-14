package com.evandhardspace.core.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

context(vm: ViewModel)
fun <T> Flow<T>.stateInUi(
    initialValue: T,
): StateFlow<T> = stateIn(
    scope = vm.viewModelScope,
    started = SharingStarted.WhileSubscribed(5_000L),
    initialValue = initialValue,
)
