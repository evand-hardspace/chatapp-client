package com.evandhardspace.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.evandhardspace.auth.presentation.navigation.AuthGraphRoute
import com.evandhardspace.auth.presentation.navigation.authGraph

@Composable
fun NavigationRoot(
    modifier: Modifier = Modifier,
) {
    val navController = rememberNavController()
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
