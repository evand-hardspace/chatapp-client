package com.evandhardspace.chatapp

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.evandhardspace.chatapp.navigation.DeeplinkListener
import com.evandhardspace.chatapp.navigation.NavigationRoot
import com.evandhardspace.core.designsystem.component.layout.ChatAppSnackbarScaffold
import com.evandhardspace.core.designsystem.component.snackbar.ChatAppSnackbarHostState
import com.evandhardspace.core.designsystem.component.snackbar.LocalSnackbarHostState
import com.evandhardspace.core.designsystem.theme.ChatAppTheme

@Composable
@Preview
fun App(): Unit = ChatAppTheme {
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember {
        ChatAppSnackbarHostState(
            snackbarHostState = SnackbarHostState(),
            scope = coroutineScope,
        )
    }

    val navController = rememberNavController()
    DeeplinkListener(navController)

    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        ChatAppSnackbarScaffold(
            snackbarHostState = snackbarHostState,
        ) {
            NavigationRoot(navController)
        }
    }
}
