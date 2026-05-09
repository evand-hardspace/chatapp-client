package com.evandhardspace.core.presentation.util.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
internal class ScopedStoreRegistry: ViewModel() {

    private val stores = mutableMapOf<String, ViewModelStore>()

    fun getOrCreate(id: String): ViewModelStore =
        stores.getOrPut(id) { ViewModelStore() }

    fun clear(id: String) {
        stores.remove(id)?.clear()
    }

    override fun onCleared() {
        super.onCleared()
        stores.values.forEach { it.clear() }
        stores.clear()
    }
}
