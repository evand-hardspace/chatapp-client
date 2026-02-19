package com.evandhardspace.auth.presentation.forgot_password

internal sealed interface ForgotPasswordAction {
    data object OnSubmitClick: ForgotPasswordAction
}
