package com.evandhardspace.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.evandhardspace.auth.presentation.email_verifiaction.EmailVerificationScreen
import com.evandhardspace.auth.presentation.email_verifiaction.util.emailVerificationDeeplinkPatternChatappScheme
import com.evandhardspace.auth.presentation.email_verifiaction.util.emailVerificationDeeplinkPatternHttpsScheme
import com.evandhardspace.auth.presentation.register.RegisterScreen
import com.evandhardspace.auth.presentation.register_success.RegisterSuccessScreen

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
        composable<AuthGraphRoute.EmailVerification>(
            deepLinks = listOf(
                navDeepLink {
                    this.uriPattern = emailVerificationDeeplinkPatternHttpsScheme
                },
                navDeepLink {
                    this.uriPattern = emailVerificationDeeplinkPatternChatappScheme
                }
            )
        ) {
            EmailVerificationScreen()
        }
    }
}
