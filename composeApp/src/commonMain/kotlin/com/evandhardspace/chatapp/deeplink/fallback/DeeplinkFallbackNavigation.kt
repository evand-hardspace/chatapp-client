package com.evandhardspace.chatapp.deeplink.fallback

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute
import com.evandhardspace.chat.presentation.navigation.ChatNavGraphRoute
import com.evandhardspace.core.navigation.NavRoute
import kotlinx.serialization.Serializable

@Serializable
private data object DeeplinkFallbackRoute : NavRoute

fun NavGraphBuilder.deeplinkFallbackScreen(navController: NavController) {
    composable<DeeplinkFallbackRoute> {
        DeeplinkFallbackScreen(
            navigateBack = { isAuthorized ->
                navController.popBackStackOrNavigateToRoot(isAuthorized)
            },
        )
    }
}

internal fun NavController.navigateToDeeplinkFallback() {
    navigate(DeeplinkFallbackRoute) {
        launchSingleTop = true
    }
}

private fun NavController.popBackStackOrNavigateToRoot(isAuthorized: Boolean) {
    if (popBackStack()) return

    if (isAuthorized) {
        navigate(ChatNavGraphRoute.Root) {
            popUpTo(ChatNavGraphRoute.Root) {
                inclusive = true
            }
        }
    } else {
        navigate(AuthNavGraphRoute.Root) {
            popUpTo(AuthNavGraphRoute.Root) {
                inclusive = true
            }
        }
    }
}
