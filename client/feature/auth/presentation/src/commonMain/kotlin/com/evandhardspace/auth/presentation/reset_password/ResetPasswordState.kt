package com.evandhardspace.auth.presentation.reset_password

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.core.presentation.util.UiText


data class ResetPasswordState(
    val passwordTextState: TextFieldState = TextFieldState(),
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isPasswordVisible: Boolean = false,
    val canSubmit: Boolean = false,
    val isPasswordChanged: Boolean = false,
)
