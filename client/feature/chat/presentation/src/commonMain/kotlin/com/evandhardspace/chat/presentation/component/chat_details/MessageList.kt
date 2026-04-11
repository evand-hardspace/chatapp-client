package com.evandhardspace.chat.presentation.component.chat_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import chatapp.client.feature.chat.presentation.generated.resources.Res
import chatapp.client.feature.chat.presentation.generated.resources.retry
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.designsystem.component.ChatAppLoadingIndicator
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MessageList(
    messages: List<MessageUi>,
    messageWithOpenMenu: MessageUi.LocalUserMessage?,
    paginationError: String?,
    isPaginationLoading: Boolean,
    listState: LazyListState,
    onRetryPagination: () -> Unit,
    onMessageLongClick: (MessageUi.LocalUserMessage) -> Unit,
    onMessageRetryClick: (MessageUi.LocalUserMessage) -> Unit,
    onDeleteMessageClick: (MessageUi.LocalUserMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    modifier: Modifier = Modifier,
    emptyContent: @Composable () -> Unit,
) {
    if (messages.isEmpty()) {
        Box(
            modifier = modifier
                .padding(vertical = MaterialTheme.paddings.double),
            contentAlignment = Alignment.Center,
        ) { emptyContent() }
        return
    }

    LazyColumn(
        modifier = modifier,
        state = listState,
        reverseLayout = true,
        contentPadding = PaddingValues(MaterialTheme.paddings.default),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.default),
    ) {
        items(
            items = messages,
            key = MessageUi::id,
        ) { message ->
            MessageListItemUi(
                messageUi = message,
                showMenu = message == messageWithOpenMenu,
                onMessageLongClick = onMessageLongClick,
                onDeleteClick = onDeleteMessageClick,
                onRetryClick = onMessageRetryClick,
                onDismissMessageMenu = onDismissMessageMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem()
            )
        }

        when {
            isPaginationLoading -> {
                item {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        ChatAppLoadingIndicator()
                    }
                }
            }
            paginationError != null -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        ChatAppButton(
                            text = stringResource(Res.string.retry),
                            onClick = onRetryPagination,
                            style = ChatAppButtonStyle.Secondary,
                        )
                        Spacer(Modifier.height(MaterialTheme.paddings.half))
                        Text(
                            text = paginationError,
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            textAlign = TextAlign.Center,
                        )
                    }
                }
            }
        }
    }
}
