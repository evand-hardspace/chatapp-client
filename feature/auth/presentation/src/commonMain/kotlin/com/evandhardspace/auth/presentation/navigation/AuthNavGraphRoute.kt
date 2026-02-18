package com.evandhardspace.auth.presentation.navigation

import com.evandhardspace.core.presentation.navigation.NavRoute
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

sealed interface AuthNavGraphRoute: NavRoute {
    @Serializable
    data object Root : AuthNavGraphRoute

    @Serializable
    data object Login : AuthNavGraphRoute

    @Serializable
    data object Register : AuthNavGraphRoute

    @Serializable
    data class RegisterSuccess(
        @SerialName(EMAIL_ARG_KEY)
        val email: String,
    ) : AuthNavGraphRoute {
        companion object {
            const val EMAIL_ARG_KEY = "email"
        }
    }

    @Serializable
    data object ResetPassword : AuthNavGraphRoute

    @Serializable
    data object ForgotPassword : AuthNavGraphRoute

    @Serializable
    data class EmailVerification(
        @SerialName(TOKEN_ARG_KEY)
        val token: String,
    ) : AuthNavGraphRoute {
        companion object {
            const val TOKEN_ARG_KEY = "token"
        }
    }
}
