package com.evandhardspace.chat.presentation.component.chat_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.asUiText

@Composable
internal fun MessageListItemUi(
    messageUi: MessageUi,
    onMessageLongClick: (MessageUi.LocalUserMessage) -> Unit,
    onDeleteClick: (MessageUi.LocalUserMessage) -> Unit,
    onRetryClick: (MessageUi.LocalUserMessage) -> Unit,
    onDismissMessageMenu: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier,
    ) {
        when (messageUi) {
            is MessageUi.DateSeparator -> {
                DateSeparatorUi(
                    date = messageUi.date.asComposableString(),
                    modifier = Modifier.fillMaxWidth(),
                )
            }

            is MessageUi.LocalUserMessage -> {
                LocalUserMessage(
                    message = messageUi,
                    onMessageLongClick = { onMessageLongClick(messageUi) },
                    onDismissMessageMenu = onDismissMessageMenu,
                    onDeleteClick = { onDeleteClick(messageUi) },
                    onRetryClick = { onRetryClick(messageUi) }
                )
            }

            is MessageUi.OtherUserMessage -> {
                OtherUserMessage(
                    message = messageUi,
                )
            }
        }
    }
}


@Composable
private fun DateSeparatorUi(
    date: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        HorizontalDivider(modifier = Modifier.weight(1f))
        Text(
            text = date,
            modifier = Modifier
                .padding(horizontal = MaterialTheme.paddings.double),
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.extended.textPlaceholder,
        )
        HorizontalDivider(modifier = Modifier.weight(1f))
    }
}

@ThemedPreview
@Composable
private fun MessageListItemLocalMessageUiPreview() {
    ChatAppPreview {
        MessageListItemUi(
            messageUi = MessageUi.LocalUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                deliveryStatus = DeliveryStatus.Sent,
                isMenuOpen = true,
                formattedSentTime = "Friday 2:20pm".asUiText(),
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp),
        )
    }
}

@ThemedPreview
@Composable
private fun MessageListItemLocalMessageRetryUiPreview() {
    ChatAppPreview {
        MessageListItemUi(
            messageUi = MessageUi.LocalUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                deliveryStatus = DeliveryStatus.Failed,
                isMenuOpen = false,
                formattedSentTime = "Friday 2:20pm".asUiText(),
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}

@ThemedPreview
@Composable
private fun MessageListItemOtherMessageUiPreview() {
    ChatAppPreview {
        MessageListItemUi(
            messageUi = MessageUi.OtherUserMessage(
                id = "1",
                content = "Hello world, this is a preview message that spans multiple lines",
                formattedSentTime = "Friday 2:20pm".asUiText(),
                sender = ChatParticipantUi(
                    id = "1",
                    username = "Ivan",
                    initials = "IV",
                )
            ),
            onRetryClick = {},
            onMessageLongClick = {},
            onDismissMessageMenu = {},
            onDeleteClick = {},
            modifier = Modifier
                .fillMaxWidth(),
        )
    }
}
