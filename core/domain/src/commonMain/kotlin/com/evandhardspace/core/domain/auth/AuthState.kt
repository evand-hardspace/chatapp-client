package com.evandhardspace.core.domain.auth

sealed interface AuthState {
    data object Unauthenticated: AuthState

    data class Authenticated(
        val accessToken: String,
        val refreshToken: String,
        val user: User,
    ): AuthState
}
