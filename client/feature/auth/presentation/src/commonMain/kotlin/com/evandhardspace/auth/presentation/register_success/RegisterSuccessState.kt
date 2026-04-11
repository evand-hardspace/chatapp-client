package com.evandhardspace.auth.presentation.register_success

import com.evandhardspace.core.presentation.util.UiText

internal data class RegisterSuccessState(
    val registeredEmail: String = "",
    val isResendingVerificationEmail: Boolean = false,
    val resendVerificationError: UiText? = null,
)
