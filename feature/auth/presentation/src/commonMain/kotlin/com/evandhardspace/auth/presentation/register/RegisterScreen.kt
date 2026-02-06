package com.evandhardspace.auth.presentation.register

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterContent(
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun RegisterContent(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
) {

}

@ThemedPreview
@Composable
private fun Preview() {
    ChatAppPreview {
        RegisterContent(
            state = RegisterState(),
            onAction = {},
        )
    }
}
