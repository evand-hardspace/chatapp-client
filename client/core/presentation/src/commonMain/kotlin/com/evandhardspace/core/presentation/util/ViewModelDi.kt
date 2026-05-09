package com.evandhardspace.core.presentation.util

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import dev.zacsweers.metro.Assisted
import dev.zacsweers.metro.DefaultBinding
import dev.zacsweers.metrox.viewmodel.ViewModelAssistedFactory

@DefaultBinding<ViewModelAssistedFactory>
interface SavedStateHandleAssistedFactory <VM: ViewModel>: ViewModelAssistedFactory {
    fun create(@Assisted savedStateHandle: SavedStateHandle): VM

    override fun create(extras: CreationExtras): ViewModel =
        create(extras.createSavedStateHandle())
}
