package com.evandhardspace.auth.presentation.login

internal sealed interface LoginAction {
    data object OnTogglePasswordVisibility: LoginAction
    data object OnForgotPassword: LoginAction
    data object OnLogin: LoginAction
    data object OnSignUp: LoginAction
}
