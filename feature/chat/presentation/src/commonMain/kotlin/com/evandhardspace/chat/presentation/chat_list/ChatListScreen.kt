package com.evandhardspace.chat.presentation.chat_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.cancel
import chatapp.feature.chat.presentation.generated.resources.create_chat
import chatapp.feature.chat.presentation.generated.resources.do_you_want_to_logout
import chatapp.feature.chat.presentation.generated.resources.do_you_want_to_logout_description
import chatapp.feature.chat.presentation.generated.resources.logout
import chatapp.feature.chat.presentation.generated.resources.no_chats
import chatapp.feature.chat.presentation.generated.resources.no_chats_subtitle
import com.evandhardspace.chat.presentation.component.chat_details.EmptyContentSection
import com.evandhardspace.chat.presentation.component.chat_list.ChatListHeader
import com.evandhardspace.chat.presentation.component.chat_list.ChatListItemUi
import com.evandhardspace.chat.presentation.model.ChatUi
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppHorizontalDivider
import com.evandhardspace.core.designsystem.component.button.ChatAppFloatingActionButton
import com.evandhardspace.core.designsystem.component.dialog.ChatAppDialog
import com.evandhardspace.core.designsystem.theme.ChatAppTheme
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ChatListScreen(
    viewModel: ChatListViewModel,
    onChatClick: (ChatUi) -> Unit,
    onConfirmLogoutClick: () -> Unit,
    onCreateChatClick: () -> Unit,
    onProfileSettingsClick: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    val snackbarHostState = remember { SnackbarHostState() }

    ChatListContent(
        state = state,
        onAction = viewModel::onAction,
        onChatClick = onChatClick,
        onConfirmLogoutClick = onConfirmLogoutClick,
        onCreateChatClick = onCreateChatClick,
        onProfileSettingsClick = onProfileSettingsClick,
        snackbarHostState = snackbarHostState,
    )
}

@Composable
private fun ChatListContent(
    state: ChatListState,
    onAction: (ChatListAction) -> Unit,
    onChatClick: (ChatUi) -> Unit,
    onConfirmLogoutClick: () -> Unit,
    onCreateChatClick: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    snackbarHostState: SnackbarHostState,
) {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(),
        containerColor = MaterialTheme.colorScheme.extended.surfaceLower,
        contentWindowInsets = WindowInsets.safeDrawing,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        floatingActionButton = {
            ChatAppFloatingActionButton(
                onClick = onCreateChatClick,
            ) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = stringResource(Res.string.create_chat),
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            ChatListHeader(
                modifier = Modifier.padding(top = innerPadding.calculateTopPadding()),
                localParticipant = state.localParticipant,
                isUserMenuOpen = state.isUserMenuOpen,
                onUserAvatarClick = { onAction(ChatListAction.OnUserAvatar) },
                onLogoutClick = { onAction(ChatListAction.OnLogout) },
                onDismissMenu = { onAction(ChatListAction.OnDismissUserMenu) },
                onProfileSettingsClick = onProfileSettingsClick,
            )
            when {
                state.isLoading -> {
                    CircularProgressIndicator(
                        color = MaterialTheme.colorScheme.primary,
                    )
                }

                state.chats.isEmpty() -> {
                    EmptyContentSection(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth()
                            .padding(horizontal = MaterialTheme.paddings.half),
                        title = stringResource(Res.string.no_chats),
                        description = stringResource(Res.string.no_chats_subtitle),
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentPadding = PaddingValues(bottom = innerPadding.calculateBottomPadding()),
                    ) {
                        items(
                            items = state.chats,
                            key = { it.id },
                        ) { chatUi ->
                            ChatListItemUi(
                                chat = chatUi,
                                isSelected = chatUi.id == state.selectedChatId,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onChatClick(chatUi) },
                            )
                            ChatAppHorizontalDivider()
                        }
                    }
                }
            }
        }
    }

    if (state.showLogoutConfirmation) {
        ChatAppDialog(
            title = stringResource(Res.string.do_you_want_to_logout),
            description = stringResource(Res.string.do_you_want_to_logout_description),
            confirmButtonText = stringResource(Res.string.logout),
            cancelButtonText = stringResource(Res.string.cancel),
            onDismiss = { onAction(ChatListAction.OnDismissLogoutDialog) },
            onCancelClick = { onAction(ChatListAction.OnDismissLogoutDialog) },
            onConfirmClick = onConfirmLogoutClick,
        )
    }
}

@ThemedPreview
@Composable
private fun ChatListContentPreview() {
    ChatAppTheme {
        ChatListContent(
            state = ChatListState(),
            onAction = {},
            snackbarHostState = remember { SnackbarHostState() },
            onChatClick = {},
            onConfirmLogoutClick = {},
            onCreateChatClick = {},
            onProfileSettingsClick = {},
        )
    }
}
