package com.evandhardspace.core.designsystem.component.layout

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.union
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.evandhardspace.core.designsystem.component.snackbar.ChatAppSnackbarHost
import com.evandhardspace.core.designsystem.component.snackbar.ChatAppSnackbarHostState
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
fun ChatAppSnackbarScaffold(
    snackbarHostState: ChatAppSnackbarHostState,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets.statusBars
            .union(WindowInsets.displayCutout)
            .union(WindowInsets.ime),
        snackbarHost = {
            ChatAppSnackbarHost(
                snackbarHostState = snackbarHostState,
                modifier = Modifier
                    .padding(bottom = MaterialTheme.paddings.fiveQuarters),
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding),
        ) {
            content()
        }
    }
}
