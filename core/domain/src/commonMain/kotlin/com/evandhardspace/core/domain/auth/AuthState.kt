package com.evandhardspace.core.domain.auth

sealed interface AuthState {
    data object Unauthorized: AuthState

    data class Authorized(
        val accessToken: String,
        val refreshToken: String,
        val user: User,
    ): AuthState
}
