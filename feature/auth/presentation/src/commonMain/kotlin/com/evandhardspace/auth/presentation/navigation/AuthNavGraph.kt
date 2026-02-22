package com.evandhardspace.auth.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navDeepLink
import androidx.navigation.navigation
import com.evandhardspace.auth.presentation.email_verifiaction.EmailVerificationScreen
import com.evandhardspace.auth.presentation.email_verifiaction.util.resetPasswordDeeplinkPatternChatappScheme
import com.evandhardspace.auth.presentation.email_verifiaction.util.resetPasswordDeeplinkPatternHttpsScheme
import com.evandhardspace.auth.presentation.forgot_password.ForgotPasswordScreen
import com.evandhardspace.auth.presentation.login.LoginScreen
import com.evandhardspace.auth.presentation.register.RegisterScreen
import com.evandhardspace.auth.presentation.register_success.RegisterSuccessScreen
import com.evandhardspace.auth.presentation.reset_password.ResetPasswordScreen
import com.evandhardspace.core.navigation.fullClearBackStack

fun NavGraphBuilder.authNavGraph(
    navController: NavController,
    onLoginSuccess: () -> Unit,
) {
    navigation<AuthNavGraphRoute.Root>(
        startDestination = AuthNavGraphRoute.Login,
    ) {
        composable<AuthNavGraphRoute.Login> {
            LoginScreen(
                onLoginSuccess = onLoginSuccess,
                onForgotPasswordClick = {
                    navController.navigate(AuthNavGraphRoute.ForgotPassword)
                },
                onCreateAccountClick = {
                    navController.navigate(AuthNavGraphRoute.Register) {
                        restoreState = true
                        launchSingleTop = true
                    }
                },
            )
        }

        composable<AuthNavGraphRoute.Register> {
            RegisterScreen(
                onRegisterSuccess = { email ->
                    navController.navigate(
                        route = AuthNavGraphRoute.RegisterSuccess(email),
                    )
                },
                onLogin = {
                    navController.navigate(AuthNavGraphRoute.Login) {
                        popUpTo(AuthNavGraphRoute.Register) {
                            inclusive = true
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }

        composable<AuthNavGraphRoute.RegisterSuccess> {
            RegisterSuccessScreen(
                navigateToLogin = {
                    navController.navigate(AuthNavGraphRoute.Login) {
                        popUpTo<AuthNavGraphRoute.RegisterSuccess> {
                            inclusive = true
                        }
                    }
                }
            )
        }

        composable<AuthNavGraphRoute.EmailVerification> {
            EmailVerificationScreen(
                navigateBack = navController::popBackStack,
                navigateToLogin = {
                    navController.navigate(AuthNavGraphRoute.Login) {
                        navController.fullClearBackStack()
                    }
                },
            )
        }

        composable<AuthNavGraphRoute.ForgotPassword> {
            ForgotPasswordScreen()
        }
        composable<AuthNavGraphRoute.ResetPassword>(
            deepLinks = listOf(
                navDeepLink { this.uriPattern = resetPasswordDeeplinkPatternHttpsScheme },
                navDeepLink {
                    this.uriPattern = resetPasswordDeeplinkPatternChatappScheme
                },
            ),
        ) {
            ResetPasswordScreen(
                navigateToLogin = {
                    navController.navigate(AuthNavGraphRoute.Login) {
                        launchSingleTop = true
                        popUpTo<AuthNavGraphRoute.ResetPassword> {
                            inclusive = true
                        }
                    }
                }
            )
        }
    }
}
