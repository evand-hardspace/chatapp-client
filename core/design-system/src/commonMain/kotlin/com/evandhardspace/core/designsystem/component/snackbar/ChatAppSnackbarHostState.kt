package com.evandhardspace.core.designsystem.component.snackbar

import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.compositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

val LocalSnackbarHostState = compositionLocalOf {
    ChatAppSnackbarHostState(
        snackbarHostState = SnackbarHostState(),
        scope = CoroutineScope(SupervisorJob()),
    )
}

class ChatAppSnackbarHostState(
    internal val snackbarHostState: SnackbarHostState,
    private val scope: CoroutineScope,
) {
    fun show(
        message: String,
        actionLabel: String? = null,
        withDismissAction: Boolean = false,
        duration: SnackbarDuration =
            if (actionLabel == null) SnackbarDuration.Short else SnackbarDuration.Indefinite,
    ) {
        scope.launch {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = actionLabel,
                withDismissAction = withDismissAction,
                duration = duration,
            )
        }
    }
    fun dismiss() {
        scope.launch {
            snackbarHostState.currentSnackbarData?.dismiss()
        }
    }
}
