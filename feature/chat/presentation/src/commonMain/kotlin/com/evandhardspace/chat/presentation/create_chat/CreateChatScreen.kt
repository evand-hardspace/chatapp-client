package com.evandhardspace.chat.presentation.create_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.cancel
import chatapp.feature.chat.presentation.generated.resources.create_chat
import com.evandhardspace.chat.presentation.component.ChatParticipantSearchTextSection
import com.evandhardspace.chat.presentation.component.ChatParticipantsSelectionSection
import com.evandhardspace.chat.presentation.component.ManageChatButtonSection
import com.evandhardspace.chat.presentation.component.ManageChatHeaderRow
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppHorizontalDivider
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.dialog.ChatAppAdaptiveDialogSheetLayout
import com.evandhardspace.core.designsystem.component.dialog.rememberAdaptiveDialogSheetController
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.presentation.util.DeviceConfiguration
import com.evandhardspace.core.presentation.util.OnEffect
import com.evandhardspace.core.presentation.util.compose.clearFocusOnTap
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration
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
        CreateChatContent(
            state = state,
            onAction = viewModel::onAction,
            onDismiss = dialogSheetController::dismiss,
        )
    }
}

@Composable
private fun CreateChatContent(
    state: CreateChatState,
    onAction: (CreateChatAction) -> Unit,
    onDismiss: () -> Unit,
) {
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = imeHeight > 0
    val configuration = currentDeviceConfiguration()

    val shouldHideHeader = configuration == DeviceConfiguration.MobileLandscape
            && (isKeyboardVisible || isTextFieldFocused)

    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxWidth()
            .wrapContentHeight()
            .imePadding()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding(),
    ) {
        AnimatedVisibility(
            visible = shouldHideHeader.not(),
        ) {
            Column {
                ManageChatHeaderRow(
                    title = stringResource(Res.string.create_chat),
                    onCloseClick = onDismiss,
                    modifier = Modifier.fillMaxWidth()
                )
                ChatAppHorizontalDivider()
            }
        }
        ChatParticipantSearchTextSection(
            queryState = state.queryTextState,
            onAddClick = { onAction(CreateChatAction.OnAdd) },
            isSearchEnabled = state.canAddParticipant,
            isLoading = state.isSearching,
            modifier = Modifier
                .fillMaxWidth(),
            error = state.searchError,
            onFocusChanged = {
                isTextFieldFocused = it
            }
        )

        ChatAppHorizontalDivider()
        ChatParticipantsSelectionSection(
            selectedParticipants = state.selectedChatParticipants,
            modifier = Modifier
                .fillMaxWidth(),
            searchResult = state.currentSearchResult
        )

        ChatAppHorizontalDivider()
        ManageChatButtonSection(
            primaryButton = {
                ChatAppButton(
                    text = stringResource(Res.string.create_chat),
                    onClick = {
                        onAction(CreateChatAction.OnCreateChat)
                    },
                    enabled = state.selectedChatParticipants.isNotEmpty(),
                    isLoading = state.isCreatingChat
                )
            },
            secondaryButton = {
                ChatAppButton(
                    text = stringResource(Res.string.cancel),
                    onClick = onDismiss,
                    style = ChatAppButtonStyle.Secondary,
                )
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@ThemedPreview
@Composable
private fun CreateChatScreenPreview() {
    ChatAppPreview {
        CreateChatContent(
            state = CreateChatState(),
            onAction = {},
            onDismiss = {},
        )
    }
}
