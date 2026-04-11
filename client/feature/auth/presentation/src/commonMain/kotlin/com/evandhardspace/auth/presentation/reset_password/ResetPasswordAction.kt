package com.evandhardspace.auth.presentation.reset_password

internal sealed interface ResetPasswordAction {
    data object OnSubmit: ResetPasswordAction
    data object OnTogglePasswordVisibility: ResetPasswordAction
}
