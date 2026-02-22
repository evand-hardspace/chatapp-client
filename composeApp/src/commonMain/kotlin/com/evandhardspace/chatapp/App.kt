package com.evandhardspace.chatapp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute
import com.evandhardspace.chat.presentation.navigation.ChatNavGraphRoute
import com.evandhardspace.chatapp.deeplink.DeeplinkListener
import com.evandhardspace.chatapp.navigation.NavigationRoot
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.layout.ChatAppSnackbarScaffold
import com.evandhardspace.core.designsystem.component.snackbar.ChatAppSnackbarHostState
import com.evandhardspace.core.designsystem.component.snackbar.LocalSnackbarHostState
import com.evandhardspace.core.designsystem.theme.ChatAppTheme
import com.evandhardspace.core.navigation.deeplink.DeeplinkProcessor
import com.evandhardspace.core.presentation.util.OnEffect
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@ThemedPreview
@Composable
fun App(
    onAuthenticationChecked: () -> Unit = {},
    viewModel: MainViewModel = koinViewModel(),
    deeplinkManager: DeeplinkProcessor = koinInject(),
): Unit = ChatAppTheme {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember {
        ChatAppSnackbarHostState(
            snackbarHostState = SnackbarHostState(),
            scope = coroutineScope,
        )
    }
    val navController = rememberNavController()
    val state by viewModel.state.collectAsStateWithLifecycle()

    LaunchedEffect(state) {
        val currentState = state
        if (currentState !is MainState.Loading) {
            onAuthenticationChecked()
        }
    }

    OnEffect(viewModel.effects) { effect ->
        when (effect) {
            is MainEffect.LoggedOut -> {
                navController.navigate(AuthNavGraphRoute.Root) {
                    popUpTo(AuthNavGraphRoute.Root) {
                        inclusive = false
                    }
                }
            }
        }
    }

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        ChatAppSnackbarScaffold(
            snackbarHostState = snackbarHostState,
        ) {
            when (val currentState = state) {
                is MainState.Loaded -> {
                    NavigationRoot(
                        navController = navController,
                        startDestination = if (currentState.isAuthorized) ChatNavGraphRoute.Root
                        else AuthNavGraphRoute.Root,
                    )
                    DeeplinkListener(deeplinkManager, navController)
                }

                is MainState.Loading -> Unit // TODO(9): Add loader
            }
        }
    }
}

