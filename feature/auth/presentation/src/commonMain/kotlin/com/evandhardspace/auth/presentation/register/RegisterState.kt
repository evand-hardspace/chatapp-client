package com.evandhardspace.auth.presentation.register

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.core.presentation.util.UiText

internal data class RegisterState(
    val emailTextState: TextFieldState = TextFieldState(),
    val emailError: UiText? = null,
    val passwordTextState: TextFieldState = TextFieldState(),
    val passwordError: UiText? = null,
    val usernameTextState: TextFieldState = TextFieldState(),
    val usernameError: UiText? = null,
    val registrationError: UiText? = null,
    val isRegistering: Boolean = false,
    val isPasswordVisible: Boolean = false,
) {
    val isEmailValid: Boolean = emailError == null
    val isPasswordValid: Boolean = passwordError == null
    val isUsernameValid: Boolean = usernameError == null
    val canRegister: Boolean = isRegistering.not()
            && isEmailValid
            && isPasswordValid
            && isUsernameValid

    val canLogin: Boolean = canRegister
}
