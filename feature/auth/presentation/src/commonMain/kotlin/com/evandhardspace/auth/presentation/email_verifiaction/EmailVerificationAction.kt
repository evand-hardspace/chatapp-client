package com.evandhardspace.auth.presentation.email_verifiaction

internal sealed interface EmailVerificationAction {
    data object OnLogin: EmailVerificationAction
    data object OnClose: EmailVerificationAction
}