package com.evandhardspace.auth.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface AuthGraphRoute {
    @Serializable
    data object Root : AuthGraphRoute

    @Serializable
    data object Login: AuthGraphRoute

    @Serializable
    data object Register: AuthGraphRoute

    @Serializable
    data class RegisterSuccess(val email: String): AuthGraphRoute

    @Serializable
    data object ResetPassword: AuthGraphRoute

    @Serializable
    data class EmailVerification(val token: String): AuthGraphRoute
}
