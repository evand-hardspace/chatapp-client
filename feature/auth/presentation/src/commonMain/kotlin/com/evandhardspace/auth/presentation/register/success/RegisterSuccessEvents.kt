package com.evandhardspace.auth.presentation.register.success

sealed interface RegisterSuccessEffect {
    data object ResendVerificationEmailSuccess: RegisterSuccessEffect
}
