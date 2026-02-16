package com.evandhardspace.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.evandhardspace.auth.presentation.navigation.AuthGraphRoute
import com.evandhardspace.auth.presentation.navigation.authGraph
import com.evandhardspace.core.data.logging.KermitLogger

@Composable
fun NavigationRoot(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = AuthGraphRoute.Root,
    ) {
        authGraph(
            navController,
            onLoginSuccess = { },
        )
    }
}
