package com.evandhardspace.chatapp

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.evandhardspace.auth.presentation.register.RegisterRoot
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
    CompositionLocalProvider(
        LocalSnackbarHostState provides snackbarHostState,
    ) {
        ChatAppSnackbarScaffold(
            snackbarHostState = snackbarHostState,
        ) {
            RegisterRoot(
                modifier = Modifier.fillMaxSize(),
                onRegisterSuccess = { },
            )
        }
    }
}
