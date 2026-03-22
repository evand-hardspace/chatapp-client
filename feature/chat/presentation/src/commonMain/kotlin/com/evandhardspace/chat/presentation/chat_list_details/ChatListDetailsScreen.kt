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
import com.evandhardspace.chat.presentation.chat_list.ChatListAction
import com.evandhardspace.chat.presentation.chat_list.ChatListScreen
import com.evandhardspace.chat.presentation.chat_list.ChatListViewModel
import com.evandhardspace.chat.presentation.create_chat.CreateChatScreen
import com.evandhardspace.chat.presentation.manage_chat.ManageChatScreen
import com.evandhardspace.core.designsystem.component.layout.NavigableListDetailPaneScaffold
import com.evandhardspace.core.designsystem.component.modifier.clipOnTransition
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.presentation.util.dialog.DialogViewModelScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ChatListDetailsScreen(
    chatListDetailViewModel: ChatListDetailsSharedViewModel = koinViewModel(),
    onLogout: () -> Unit,
) {
    val state by chatListDetailViewModel.state.collectAsStateWithLifecycle()
    ChatListDetailsContent(
        state = state,
        action = chatListDetailViewModel::onAction,
        onLogout = onLogout,
    )
}

@Composable
private fun ChatListDetailsContent(
    state: ChatListDetailsState,
    action: (ChatListDetailsAction) -> Unit,
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
    val chatListViewModel = koinViewModel<ChatListViewModel>()

    val onBack = {
        chatDetailViewModel.onAction(ChatDetailsAction.OnSelectChat(null))
        chatListViewModel.onAction(ChatListAction.OnSelectChat(null))
    } // TODO: reorganize events

    LaunchedEffect(detailPaneValue, state.selectedChatId) {
        if (detailPaneValue == PaneAdaptedValue.Hidden && state.selectedChatId != null) {
            action(ChatListDetailsAction.OnOpenChat(null))
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.extended.surfaceLower),
        onBackCompleted = onBack,
        listPane = {
            AnimatedPane {
                ChatListScreen(
                    viewModel = chatListViewModel,
                    onChatClick = { chat ->
                        action(ChatListDetailsAction.OnOpenChat(chat.id))
                        chatListViewModel.onAction(ChatListAction.OnSelectChat(chat))
                        scope.launch {
                            scaffoldNavigator.navigateTo(
                                pane = ListDetailPaneScaffoldRole.Detail,
                            )
                        }
                    },
                    onConfirmLogoutClick = onLogout,
                    onCreateChatClick = { action(ChatListDetailsAction.OnCreateChat) },
                    onProfileSettingsClick = { action(ChatListDetailsAction.OnOpenProfileSettings) },
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
                                onBack()
                                scaffoldNavigator.navigateBack()
                            }
                        }
                    },
                    onManageChat = {
                        action(ChatListDetailsAction.OnManageChat)
                    }
                )
            }
        }
    )

    DialogViewModelScope(
        visible = state.dialogState is DialogState.CreateChat,
    ) {
        CreateChatScreen(
            onDismiss = { action(ChatListDetailsAction.OnDismissCurrentDialog) },
            onChatCreated = { chatId ->
                action(ChatListDetailsAction.OnDismissCurrentDialog)
                action(ChatListDetailsAction.OnOpenChat(chatId))
                scope.launch {
                    scaffoldNavigator.navigateTo(ListDetailPaneScaffoldRole.Detail)
                }
            }
        )
    }

    LaunchedEffect(state.dialogState) {
        when(state.dialogState) {
            is DialogState.ManageChat -> chatDetailViewModel.onAction(ChatDetailsAction.OnDismissChatOptions)
            else -> Unit
        }
    }

    DialogViewModelScope(
        visible = state.dialogState is DialogState.ManageChat,
    ) {
        ManageChatScreen(
            onDismiss = { action(ChatListDetailsAction.OnDismissCurrentDialog) },
        )
    }
}
