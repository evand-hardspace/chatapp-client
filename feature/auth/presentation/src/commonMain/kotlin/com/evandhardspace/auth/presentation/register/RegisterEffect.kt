package com.evandhardspace.auth.presentation.register

sealed interface RegisterEffect {
    data class Success(val email: String) : RegisterEffect
}
