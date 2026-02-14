package com.evandhardspace.auth.presentation.register.success

data class RegisterSuccessState(
    val registeredEmail: String = "",
    val isResendingVerificationEmail: Boolean = false,
)
