package com.evandhardspace.auth.presentation.login

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

internal class LoginViewModel : ViewModel() {

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLogin -> Unit
            LoginAction.OnTogglePasswordVisibility -> Unit
            LoginAction.OnForgotPassword -> Unit
            LoginAction.OnSignUp -> Unit
        }
    }
}
