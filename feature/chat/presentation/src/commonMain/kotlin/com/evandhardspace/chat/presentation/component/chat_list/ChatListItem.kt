package com.evandhardspace.chat.presentation.component.chat_list

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.group_chat
import chatapp.feature.chat.presentation.generated.resources.you
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.presentation.model.ChatUi
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.avatar.ChatAppStackedAvatars
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.designsystem.theme.titleXSmall
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock

@Composable
fun ChatListItemUi(
    chat: ChatUi,
    isSelected: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .height(IntrinsicSize.Min)
            .background(
                color = if (isSelected) MaterialTheme.colorScheme.surface
                else MaterialTheme.colorScheme.extended.surfaceLower

            )
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(MaterialTheme.paddings.default),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.default),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
            ) {
                ChatAppStackedAvatars(
                    avatars = chat.otherParticipants,
                )
                Column(
                    modifier = Modifier
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.quarter),
                ) {
                    Text(
                        text = if (chat.isGroupChat) {
                            chat.otherParticipants.first().username
                        } else {
                            stringResource(Res.string.group_chat)
                        },
                        style = MaterialTheme.typography.titleXSmall,
                        color = MaterialTheme.colorScheme.extended.textPrimary,
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1,
                        modifier = Modifier
                            .fillMaxWidth(),
                    )
                    if (chat.isGroupChat) {
                        val you = stringResource(Res.string.you)
                        val formattedUsernames = remember(chat.otherParticipants) {
                            "$you, " + chat.otherParticipants.joinToString { it.username }
                        }
                        Text(
                            text = formattedUsernames,
                            color = MaterialTheme.colorScheme.extended.textPlaceholder,
                            style = MaterialTheme.typography.bodySmall,
                            modifier = Modifier.fillMaxWidth(),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                }
            }

            if (chat.latestMessage != null) {
                val previewMessage = buildAnnotatedString {
                    withStyle(
                        style = SpanStyle(
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.extended.textSecondary,
                        )
                    ) {
                        append(chat.latestMessageSenderUsername + ":")
                    }
                    append(chat.latestMessage.content)
                }
                Text(
                    text = previewMessage,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Box(
            modifier = Modifier
                .alpha(if (isSelected) 1f else 0f)
                .background(MaterialTheme.colorScheme.primary)
                .width(4.dp)
                .fillMaxHeight()
        )
    }
}

@ThemedPreview
@Composable
fun ChatListItemPreview() {
    ChatAppPreview {
        ChatListItemUi(
            isSelected = true,
            modifier = Modifier
                .fillMaxWidth(),
            chat = ChatUi(
                id = "1",
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Ivan",
                    initials = "IV",
                ),
                otherParticipants = listOf(
                    ChatParticipantUi(
                        id = "2",
                        username = "Charly",
                        initials = "CH",
                    ),
                    ChatParticipantUi(
                        id = "3",
                        username = "Josh",
                        initials = "JO",
                    )
                ),
                latestMessage = ChatMessage(
                    id = "1",
                    chatId = "1",
                    content = "This is a last chat message that was sent by Ivan " +
                            "and goes over multiple lines to showcase the ellipsis",
                    createdAt = Clock.System.now(),
                    senderId = "1",
                ),
                latestMessageSenderUsername = "Ivan",
            )
        )
    }
}