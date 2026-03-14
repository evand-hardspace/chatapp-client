package com.evandhardspace.chat.presentation.chat_list_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.layout.AnimatedPane
import androidx.compose.material3.adaptive.layout.ListDetailPaneScaffoldRole
import androidx.compose.material3.adaptive.navigation.rememberListDetailPaneScaffoldNavigator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.evandhardspace.chat.presentation.create_chat.CreateChatScreen
import com.evandhardspace.core.designsystem.component.layout.NavigableListDetailPaneScaffold
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.dialog.DialogViewModelScope
import kotlinx.coroutines.launch
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ChatListDetailsScreen(
    chatListDetailViewModel: ChatListDetailsViewModel = koinViewModel()
) {
    val state by chatListDetailViewModel.state.collectAsStateWithLifecycle()
    ChatListDetailsContent(
        state = state,
        onAction = chatListDetailViewModel::onAction,
    )
}

@Composable
private fun ChatListDetailsContent(
    state: ChatListDetailsState,
    onAction: (ChatListDetailsAction) -> Unit,
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
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize(),
                ) {
                    items(100) { chatIndex ->
                        Text(
                            text = "Chat $chatIndex",
                            modifier = Modifier
                                .clickable {
                                    onAction(ChatListDetailsAction.OnCreateChat)
                                    onAction(ChatListDetailsAction.OnOpenChat(chatIndex.toString()))
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
        },
        detailPane = {
            AnimatedPane {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
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
            onDismiss = { onAction(ChatListDetailsAction.OnDismissCurrentDialog) },
        )
    }
}
