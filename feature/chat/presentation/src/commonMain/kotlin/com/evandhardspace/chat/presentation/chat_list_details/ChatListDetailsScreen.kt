package com.evandhardspace.chat.presentation.chat_list_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.create_chat
import com.evandhardspace.chat.presentation.create_chat.CreateChatScreen
import com.evandhardspace.core.designsystem.component.button.ChatAppFloatingActionButton
import com.evandhardspace.core.designsystem.component.layout.NavigableListDetailPaneScaffold
import com.evandhardspace.core.designsystem.component.modifier.clipOnTransition
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.dialog.DialogViewModelScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ChatListDetailsScreen(
    chatListDetailViewModel: ChatListDetailsViewModel = koinViewModel()
) {
    val state by chatListDetailViewModel.state.collectAsStateWithLifecycle()
    ChatListDetailsContent(
        state = state,
        action = chatListDetailViewModel::onAction,
    )
}

@Composable
private fun ChatListDetailsContent(
    state: ChatListDetailsState,
    action: (ChatListDetailsAction) -> Unit,
) {
    val scaffoldDirective = createNoSpacingPaneScaffoldDirective()
    val scaffoldNavigator = rememberListDetailPaneScaffoldNavigator(
        scaffoldDirective = scaffoldDirective,
    )
    val scope = rememberCoroutineScope()

    NavigableListDetailPaneScaffold(
        navigator = scaffoldNavigator,
        modifier = Modifier
            .background(MaterialTheme.colorScheme.extended.surfaceLower),
        listPane = {
            AnimatedPane {
                Scaffold(
                    modifier = Modifier
                        .fillMaxSize(),
                    floatingActionButton = {
                        ChatAppFloatingActionButton(
                            onClick = {
                                action(ChatListDetailsAction.OnCreateChat)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(Res.string.create_chat),
                            )
                        }
                    }
                ) { innerPadding ->
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize(),
                        contentPadding = innerPadding,
                    ) {
                        items(100) { chatIndex ->
                            Text(
                                text = "Chat $chatIndex",
                                modifier = Modifier
                                    .clickable {
                                        action(ChatListDetailsAction.OnOpenChat(chatIndex.toString()))
                                        scope.launch {
                                            scaffoldNavigator.navigateTo(
                                                ListDetailPaneScaffoldRole.Detail,
                                            )
                                        }
                                    }
                                    .padding(MaterialTheme.paddings.default)
                            )
                        }
                    }
                }
            }
        },
        detailPane = {
            AnimatedPane {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clipOnTransition(32.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center,
                ) {
                    state.selectedChatId?.let {
                        Text(text = it)
                    }
                }
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
