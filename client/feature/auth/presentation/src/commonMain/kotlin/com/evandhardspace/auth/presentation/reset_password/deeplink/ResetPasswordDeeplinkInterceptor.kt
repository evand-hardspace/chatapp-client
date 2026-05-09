package com.evandhardspace.auth.presentation.reset_password.deeplink

import androidx.navigation.NavController
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.navigation.deeplink.DeeplinkInterceptor
import com.evandhardspace.core.navigation.fullClearBackStack
import com.evandhardspace.url_util.asUrl
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoSet
import dev.zacsweers.metro.Inject
import kotlinx.coroutines.flow.first

internal const val resetPasswordDeeplinkPatternHttpsScheme =
    "https://chatapp.evandhardspace.com/api/auth/reset-password"
internal const val resetPasswordDeeplinkPatternChatappScheme =
    "chatapp://chatapp.evandhardspace.com/api/auth/reset-password"

@ContributesIntoSet(AppScope::class)
@Inject
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
