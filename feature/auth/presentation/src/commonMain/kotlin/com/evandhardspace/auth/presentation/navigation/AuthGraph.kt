package com.evandhardspace.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.evandhardspace.auth.presentation.register.RegisterScreen
import com.evandhardspace.auth.presentation.register.success.RegisterSuccessScreen

fun NavGraphBuilder.authGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthGraphRoute.Root>(
        startDestination = AuthGraphRoute.Register,
    ) {
        composable<AuthGraphRoute.Register> {
            RegisterScreen(
                onRegisterSuccess = { email ->
                    navController.navigate(
                        route = AuthGraphRoute.RegisterSuccess(email),
                    )
                }
            )
        }
        composable<AuthGraphRoute.RegisterSuccess> {
            RegisterSuccessScreen()
        }
    }
}
