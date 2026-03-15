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
import com.evandhardspace.core.designsystem.component.layout.NavigableListDetailPaneScaffold
import com.evandhardspace.core.designsystem.component.modifier.clipOnTransition
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.presentation.util.dialog.DialogViewModelScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ChatListDetailsScreen(
    chatListDetailViewModel: ChatListDetailsViewModel = koinViewModel(),
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

    LaunchedEffect(detailPaneValue, state.selectedChatId) {
        if (detailPaneValue == PaneAdaptedValue.Hidden && state.selectedChatId != null) {
            action(ChatListDetailsAction.OnOpenChat(null))
        }
    }

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.extended.surfaceLower),
        onBackCompleted = {
            chatDetailViewModel.onAction(ChatDetailsAction.OnSelectChat(null))
        },
        listPane = {
            AnimatedPane {
                ChatListScreen(
                    onChatClick = {
                        action(ChatListDetailsAction.OnOpenChat(it.id))
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
                                scaffoldNavigator.navigateBack()
                            }
                        }
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
}
