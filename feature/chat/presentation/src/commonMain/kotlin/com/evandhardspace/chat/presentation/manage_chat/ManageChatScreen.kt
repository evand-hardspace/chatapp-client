package com.evandhardspace.chat.presentation.manage_chat

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.chat_members
import chatapp.feature.chat.presentation.generated.resources.save
import com.evandhardspace.chat.presentation.component.manage_chat.ManageChatContent
import com.evandhardspace.chat.presentation.create_chat.CreateChatEffect
import com.evandhardspace.core.designsystem.component.dialog.ChatAppAdaptiveDialogSheetLayout
import com.evandhardspace.core.designsystem.component.dialog.rememberAdaptiveDialogSheetController
import com.evandhardspace.core.presentation.util.OnEffect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ManageChatScreen(
    viewModel: ManageChatViewModel = koinViewModel(),
    onDismiss: () -> Unit,
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val dialogSheetController = rememberAdaptiveDialogSheetController(onDismiss)

    OnEffect(viewModel.effects) { effect ->
        when(effect) {
            ManageChatEffect.MembersAdded -> onDismiss()
        }
    }

    ChatAppAdaptiveDialogSheetLayout(
        controller = dialogSheetController,
    ) {
        ManageChatContent(
            state = state,
            action = viewModel::onAction,
            onDismiss = dialogSheetController::dismiss,
            primaryButtonText = stringResource(Res.string.save),
            headerText = stringResource(Res.string.chat_members),
        )
    }
}