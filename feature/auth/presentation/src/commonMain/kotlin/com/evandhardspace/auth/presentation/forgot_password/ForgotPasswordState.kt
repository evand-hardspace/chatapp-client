package com.evandhardspace.auth.presentation.forgot_password

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.core.presentation.util.UiText

internal data class ForgotPasswordState(
    val emailTextFieldState: TextFieldState = TextFieldState(),
    val canSubmit: Boolean = false,
    val isLoading: Boolean = false,
    val errorText: UiText? = null,
    val isEmailSentSuccessfully: Boolean = false
)
