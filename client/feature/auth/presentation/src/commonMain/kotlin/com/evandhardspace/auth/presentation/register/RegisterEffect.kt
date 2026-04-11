package com.evandhardspace.auth.presentation.register

internal sealed interface RegisterEffect {
    data class Success(val email: String) : RegisterEffect
}
