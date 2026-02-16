package com.evandhardspace.auth.presentation.login

internal sealed interface LoginEffect {
    object Success : LoginEffect
}