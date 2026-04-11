package com.evandhardspace.core.designsystem.component.snackbar

import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarData
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview

@Composable
fun ChatAppSnackbarHost(
    snackbarHostState: ChatAppSnackbarHostState,
    modifier: Modifier = Modifier,
) {
    SnackbarHost(
        hostState = snackbarHostState.snackbarHostState,
        modifier = modifier,
    ) { snackbarData ->
        ChatAppSnackbar(
            snackbarData = snackbarData,
        )
    }
}

@Composable
fun ChatAppSnackbar(
    snackbarData: SnackbarData,
    modifier: Modifier = Modifier,
) {
    Snackbar(
        snackbarData = snackbarData,
        modifier = modifier,
    )
}

@ThemedPreview
@Composable
fun ChatAppSnackbarPreview() {
    ChatAppPreview(paddings = true) {
        ChatAppSnackbar(
            snackbarData = object : SnackbarData {
                override val visuals: SnackbarVisuals = object : SnackbarVisuals {
                    override val message = "Preview Snackbar"
                    override val actionLabel = "Action"
                    override val withDismissAction = true
                    override val duration = SnackbarDuration.Indefinite
                }

                override fun performAction() = Unit

                override fun dismiss() = Unit
            }
        )
    }
}