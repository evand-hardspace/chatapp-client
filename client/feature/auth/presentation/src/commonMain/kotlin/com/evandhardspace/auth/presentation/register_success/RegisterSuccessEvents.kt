package com.evandhardspace.auth.presentation.register_success

internal sealed interface RegisterSuccessEffect {
    data object ResendVerificationEmailSuccess: RegisterSuccessEffect
}
