package com.evandhardspace.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.navigation.NavController
import androidx.navigation.NavUri

@Composable
fun DeeplinkListener(
    navController: NavController,
) {
    DisposableEffect(Unit) {
        ExternalUriHandler.listener = { uri ->
            navController.navigate(NavUri(uri))
        }
        onDispose { ExternalUriHandler.listener = null }
    }
}
