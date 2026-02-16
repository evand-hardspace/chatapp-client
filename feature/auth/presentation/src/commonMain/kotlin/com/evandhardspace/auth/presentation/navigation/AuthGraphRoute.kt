package com.evandhardspace.auth.presentation.navigation

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AuthGraphRoute {
    @Serializable
    data object Root : AuthGraphRoute

    @Serializable
    data object Login : AuthGraphRoute

    @Serializable
    data object Register : AuthGraphRoute

    @Serializable
    data class RegisterSuccess(
        @SerialName(EMAIL_ARG_KEY)
        val email: String,
    ) : AuthGraphRoute {
        companion object {
            const val EMAIL_ARG_KEY = "email"
        }
    }

    @Serializable
    data object ResetPassword : AuthGraphRoute

    @Serializable
    data object ForgotPassword : AuthGraphRoute

    @Serializable
    data class EmailVerification(
        @SerialName(TOKEN_ARG_KEY)
        val token: String,
    ) : AuthGraphRoute {
        companion object {
            const val TOKEN_ARG_KEY = "token"
        }
    }
}
