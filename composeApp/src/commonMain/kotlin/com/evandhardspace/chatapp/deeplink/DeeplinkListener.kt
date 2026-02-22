package com.evandhardspace.chatapp.deeplink

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import com.evandhardspace.chatapp.deeplink.fallback.navigateToDeeplinkFallback
import com.evandhardspace.core.navigation.deeplink.DeeplinkProcessor

@Composable
internal fun DeeplinkListener(
    deeplinkManager: DeeplinkProcessor,
    navController: NavController,
) {
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            deeplinkManager.process(
                uri = uri,
                navController = navController,
                onDeeplinkFallback = { navController.navigateToDeeplinkFallback() },
            )
        }
        onDispose { ExternalUriHandler.listener = null }
    }
}
