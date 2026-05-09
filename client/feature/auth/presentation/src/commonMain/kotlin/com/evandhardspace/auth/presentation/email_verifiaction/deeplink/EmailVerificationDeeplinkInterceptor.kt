package com.evandhardspace.auth.presentation.email_verifiaction.deeplink

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

private const val emailVerificationDeeplinkPatternHttpsScheme =
    "https://chatapp.evandhardspace.com/api/auth/verify"
private const val emailVerificationDeeplinkPatternChatappScheme =
    "chatapp://chatapp.evandhardspace.com/api/auth/verify"

@ContributesIntoSet(AppScope::class)
@Inject
internal class EmailVerificationDeeplinkInterceptor(
    private val sessionRepository: SessionRepository,
) : DeeplinkInterceptor {
    override suspend fun process(
        uri: String,
        navController: NavController
    ): Boolean {
        if (
            uri.startsWith(
                emailVerificationDeeplinkPatternHttpsScheme,
                emailVerificationDeeplinkPatternChatappScheme,
            ).not()
        ) return false

        val token = uri.asUrl().parameters.find { it.key == "token" }?.getSingleOrNull()

        if (token == null) return false
        val isAuthenticated = sessionRepository.authState.first() is AuthState.Authenticated
        if (isAuthenticated) {
            navController.navigate(AuthNavGraphRoute.EmailVerification(token = token)) {
                launchSingleTop = true
            }
        } else {
            navController.navigate(AuthNavGraphRoute.EmailVerification(token = token)) {
                navController.fullClearBackStack()
                launchSingleTop = true
            }
        }
        return true
    }
}
