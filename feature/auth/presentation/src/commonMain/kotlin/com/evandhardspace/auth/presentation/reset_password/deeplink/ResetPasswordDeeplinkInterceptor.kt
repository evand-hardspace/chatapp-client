package com.evandhardspace.auth.presentation.reset_password.deeplink

import androidx.navigation.NavController
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.navigation.deeplink.DeeplinkInterceptor
import com.evandhardspace.core.navigation.fullClearBackStack
import com.evandhardspace.url_util.asUrl
import kotlinx.coroutines.flow.first
import org.koin.core.annotation.Factory

// TODO(5): Change deeplink
internal const val resetPasswordDeeplinkPatternHttpsScheme =
    "https://chirp.pl-coding.com/api/auth/reset-password"
internal const val resetPasswordDeeplinkPatternChatappScheme =
    "chirp://chirp.pl-coding.com/api/auth/reset-password"

@Factory
class ResetPasswordDeeplinkInterceptor(
    private val sessionRepository: SessionRepository,
) : DeeplinkInterceptor {
    override suspend fun process(
        uri: String,
        navController: NavController
    ): Boolean {
        if (
            uri.startsWith(
                resetPasswordDeeplinkPatternHttpsScheme,
                resetPasswordDeeplinkPatternChatappScheme,
            ).not()
        ) return false

        val token = uri.asUrl().parameters.find { it.key == "token" }?.getSingleOrNull()
        if (token == null) return false

        val isAuthenticated = sessionRepository.authState.first() is AuthState.Authenticated
        if (isAuthenticated) {
            navController.navigate(
                route = AuthNavGraphRoute.ResetPasswordRestricted,
            ) {
                launchSingleTop = true
            }
        } else {
            navController.navigate(
                AuthNavGraphRoute.ResetPassword(
                    token = token,
                ),
            ) {
                navController.fullClearBackStack()
                launchSingleTop = true
            }
        }
        return true
    }
}
