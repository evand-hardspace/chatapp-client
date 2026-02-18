package com.evandhardspace.chatapp.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute
import com.evandhardspace.auth.presentation.navigation.authNavGraph
import com.evandhardspace.chat.presentation.navigation.ChatNavGraphRoute
import com.evandhardspace.chat.presentation.navigation.chatNavGraph
import com.evandhardspace.core.presentation.navigation.NavRoute

@Composable
fun NavigationRoot(
    navController: NavHostController,
    startDestination: NavRoute,
    modifier: Modifier = Modifier,
) {
    NavHost(
        modifier = modifier,
        navController = navController,
        startDestination = startDestination,
    ) {
        authNavGraph(
            navController,
            onLoginSuccess = {
                navController.navigate(ChatNavGraphRoute.ChatListRoute) {
                    popUpTo(AuthNavGraphRoute.Root) {
                        inclusive = true
                    }
                }
            },
        )
        chatNavGraph()
    }
}
