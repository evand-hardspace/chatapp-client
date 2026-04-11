package com.evandhardspace.auth.presentation.login

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.core.presentation.util.UiText

internal data class LoginState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val emailError: UiText? = null,
    val passwordTextFieldState: TextFieldState = TextFieldState(),
    val passwordError: UiText? = null,
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val error: UiText? = null,
) {
    val isEmailValid: Boolean = emailError == null
    val isPasswordValid: Boolean = passwordError == null
    val canLogin = isLoading.not() && isEmailValid && isPasswordValid
}

