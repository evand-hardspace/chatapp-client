package com.evandhardspace.core.presentation.util.dialog

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelStore
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
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
