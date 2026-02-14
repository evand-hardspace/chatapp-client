package com.evandhardspace.auth.presentation.register.success

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterSuccessViewModel : ViewModel() {

    private val _state = MutableStateFlow(RegisterSuccessState("test@test.gmail.com"))
    val state = _state.asStateFlow()

    fun onAction(action: RegisterSuccessAction) {
        when (action) {
            is RegisterSuccessAction.OnLoginClick -> Unit // TODO
            is RegisterSuccessAction.OnResendVerificationEmailClick -> Unit // TODO
        }
    }
}
