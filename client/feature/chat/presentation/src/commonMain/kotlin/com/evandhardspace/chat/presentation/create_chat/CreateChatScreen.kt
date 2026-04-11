package com.evandhardspace.chat.presentation.create_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.client.feature.chat.presentation.generated.resources.Res
import chatapp.client.feature.chat.presentation.generated.resources.create
import chatapp.client.feature.chat.presentation.generated.resources.create_chat
import com.evandhardspace.chat.presentation.component.manage_chat.ManageChatContent
import com.evandhardspace.core.designsystem.component.dialog.ChatAppAdaptiveDialogSheetLayout
import com.evandhardspace.core.designsystem.component.dialog.rememberAdaptiveDialogSheetController
import com.evandhardspace.core.presentation.util.OnEffect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun CreateChatScreen(
    viewModel: CreateChatViewModel = koinViewModel(),
    onChatCreated: (chatId: String) -> Unit,
    onDismiss: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val dialogSheetController = rememberAdaptiveDialogSheetController(onDismiss)

    OnEffect(viewModel.effects) { effect ->
        when(effect) {
            is CreateChatEffect.OnChatCreated -> onChatCreated(effect.chatId)
        }
    }

    ChatAppAdaptiveDialogSheetLayout(
        controller = dialogSheetController,
    ) {
        ManageChatContent(
            state = state,
            action = viewModel::onAction,
            onDismiss = dialogSheetController::dismiss,
            primaryButtonText = stringResource(Res.string.create),
            headerText = stringResource(Res.string.create_chat),
        )
    }
}
