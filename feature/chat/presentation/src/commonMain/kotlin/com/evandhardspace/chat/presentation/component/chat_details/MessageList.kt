package com.evandhardspace.chat.presentation.component.chat_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.no_messages
import chatapp.feature.chat.presentation.generated.resources.no_messages_subtitle
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun MessageList(
    messages: List<MessageUi>,
    listState: LazyListState,
    onMessageLongClick: (MessageUi.LocalUserMessage) -> Unit,
    onMessageRetryClick: (MessageUi.LocalUserMessage) -> Unit,
    onDeleteMessageClick: (MessageUi.LocalUserMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    if (messages.isEmpty()) {
        Box(
            modifier = modifier
                .padding(vertical = MaterialTheme.paddings.double),
            contentAlignment = Alignment.Center,
        ) {
            EmptyContentSection(
                title = stringResource(Res.string.no_messages),
                description = stringResource(Res.string.no_messages_subtitle),
            )
        }
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
                onMessageLongClick = onMessageLongClick,
                onDeleteClick = onDeleteMessageClick,
                onRetryClick = onMessageRetryClick,
                onDismissMessageMenu = onDismissMessageMenu,
                modifier = Modifier
                    .fillMaxWidth()
                    .animateItem(),
            )
        }
    }
}
