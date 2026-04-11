package com.evandhardspace.auth.presentation.register_success

internal sealed interface RegisterSuccessAction {
    data object OnResendVerificationEmail: RegisterSuccessAction
}
