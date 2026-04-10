package com.evandhardspace.chat.presentation.chat_list_details

import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.layout.PaneAdaptedValue
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.evandhardspace.chat.presentation.chat_details.ChatDetailsAction
import com.evandhardspace.chat.presentation.chat_details.ChatDetailsScreen
import com.evandhardspace.chat.presentation.chat_details.ChatDetailsViewModel
import com.evandhardspace.chat.presentation.chat_list.ChatListScreen
import com.evandhardspace.chat.presentation.create_chat.CreateChatScreen
import com.evandhardspace.chat.presentation.manage_chat.ManageChatScreen
import com.evandhardspace.chat.presentation.profile.ProfileScreen
import com.evandhardspace.core.designsystem.component.layout.NavigableListDetailPaneScaffold
import com.evandhardspace.core.designsystem.component.modifier.clipOnTransition
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.presentation.util.dialog.DialogViewModelScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ChatListDetailsScene(
    chatListDetailViewModel: SharedChatListDetailsViewModel = koinViewModel(),
    onLogout: () -> Unit,
) {
    val state by chatListDetailViewModel.state.collectAsStateWithLifecycle()
    ChatListDetailsSceneContent(
        state = state,
        action = chatListDetailViewModel::onAction,
        onLogout = onLogout,
    )
}

@Composable
private fun ChatListDetailsSceneContent(
    state: SharedChatListDetailsState,
    action: (SharedChatListDetailsAction) -> Unit,
    onLogout: () -> Unit,
) {
    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = scaffoldDirective,
    )
    val detailPaneValue = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.Detail]
    val listPaneValue = scaffoldNavigator.scaffoldValue[ListDetailPaneScaffoldRole.List]

    val scope = rememberCoroutineScope()
    val chatDetailViewModel = koinViewModel<ChatDetailsViewModel>()

    LaunchedEffect(detailPaneValue, state.selectedChatId) {
        if (detailPaneValue == PaneAdaptedValue.Hidden && state.selectedChatId != null) {
            action(SharedChatListDetailsAction.CloseChat)
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.extended.surfaceLower),
        onBackCompleted = { action(SharedChatListDetailsAction.CloseChat) },
        listPane = {
            AnimatedPane {
                ChatListScreen(
                    chatId = state.selectedChatId,
                    onChatClick = { chat ->
                        action(SharedChatListDetailsAction.OpenChat(chat.id))
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                            )
                        }
                    },
                    onConfirmLogoutClick = onLogout,
                    onCreateChatClick = { action(SharedChatListDetailsAction.CreateChat) },
                    onProfileSettingsClick = { action(SharedChatListDetailsAction.OpenProfileSettings) },
                )
            }
        },
        detailPane = {
            AnimatedPane {
                ChatDetailsScreen(
                    viewModel = chatDetailViewModel,
                    modifier = Modifier.clipOnTransition(32.dp),
                    chatId = state.selectedChatId,
                    isDetailPresent = detailPaneValue == PaneAdaptedValue.Expanded && listPaneValue == PaneAdaptedValue.Expanded,
                    onBack = {
                        scope.launch {
                            if (scaffoldNavigator.canNavigateBack()) {
                                action(SharedChatListDetailsAction.CloseChat)
                                scaffoldNavigator.navigateBack()
                            }
                        }
                    },
                    onManageChat = {
                        action(SharedChatListDetailsAction.ManageChat)
                    }
                )
            }
        }
    )

    DialogViewModelScope(
        visible = state.dialogState is DialogState.CreateChat,
    ) {
        CreateChatScreen(
            onDismiss = { action(SharedChatListDetailsAction.DismissCurrentDialog) },
            onChatCreated = { chatId ->
                action(SharedChatListDetailsAction.OpenChat(chatId))
                scope.launch {
                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                }
            }
        )
    }

    DialogViewModelScope(
        visible = state.dialogState is DialogState.Profile,
    ) {
        ProfileScreen(
            onDismiss = { action(SharedChatListDetailsAction.DismissCurrentDialog) },
        )
    }

    LaunchedEffect(state.dialogState) {
        when (state.dialogState) {
            is DialogState.ManageChat -> chatDetailViewModel.onAction(ChatDetailsAction.DismissChatOptions)
            else -> Unit
        }
    }

    DialogViewModelScope(
        visible = state.dialogState is DialogState.ManageChat,
    ) {
        ManageChatScreen(
            chatId = state.selectedChatId,
            onDismiss = { action(SharedChatListDetailsAction.DismissCurrentDialog) },
        )
    }
}
