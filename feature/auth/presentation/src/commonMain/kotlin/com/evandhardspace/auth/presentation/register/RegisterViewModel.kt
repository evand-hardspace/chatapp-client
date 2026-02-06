package com.evandhardspace.auth.presentation.register

import androidx.lifecycle.ViewModel
import com.evandhardspace.core.common.InitOwner
import com.evandhardspace.core.common.onInit
import com.evandhardspace.core.common.stateInUi
import kotlinx.coroutines.flow.MutableStateFlow

class RegisterViewModel : ViewModel() {

    private val initOwner = InitOwner()

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onInit(initOwner) {
            // load state
        }
        .stateInUi(RegisterState())

    fun onAction(action: RegisterAction) {
        when (action) {
            else -> TODO("Handle actions")
        }
    }
}
