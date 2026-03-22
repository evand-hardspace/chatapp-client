package com.evandhardspace.chat.presentation.component.chat_details

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import chatapp.core.design_system.generated.resources.arrow_left_icon
import chatapp.core.design_system.generated.resources.dots_icon
import chatapp.core.design_system.generated.resources.logout_icon
import chatapp.core.design_system.generated.resources.users_icon
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.chat_members
import chatapp.feature.chat.presentation.generated.resources.go_back
import chatapp.feature.chat.presentation.generated.resources.leave_chat
import chatapp.feature.chat.presentation.generated.resources.open_chat_options_menu
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.presentation.component.ChatHeaderContent
import com.evandhardspace.chat.presentation.component.ChatItemHeaderRow
import com.evandhardspace.chat.presentation.model.ChatUi
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.designsystem.component.button.ChatAppIconButton
import com.evandhardspace.core.designsystem.component.dropdown.ChatAppDropdownMenu
import com.evandhardspace.core.designsystem.component.dropdown.DropdownItem
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.iconSize
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import kotlin.time.Clock
import chatapp.core.design_system.generated.resources.Res as DesignSystemRes

@Composable
internal fun ChatDetailHeader(
    chatUi: ChatUi?,
    isDetailPresent: Boolean,
    isChatOptionsDropDownOpen: Boolean,
    onChatOptionsClick: () -> Unit,
    onDismissChatOptions: () -> Unit,
    onManageChatClick: () -> Unit,
    onLeaveChatClick: () -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface,
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
    ) {
        if (isDetailPresent.not()) {
            ChatAppIconButton(
                onClick = onBackClick,
                enabled = chatUi != null,
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.arrow_left_icon),
                    contentDescription = stringResource(Res.string.go_back),
                    modifier = Modifier.size(MaterialTheme.iconSize.large),
                    tint = MaterialTheme.colorScheme.extended.textSecondary,
                )
            }
        }

        if (chatUi != null) {
            val isGroupChat = chatUi.otherParticipants.size > 1
            ChatItemHeaderRow(
                chat = chatUi,
                isGroupChat = isGroupChat,
                modifier = Modifier
                    .weight(1f)
                    .clickable(onClick = onManageChatClick),
            )
        } else {
            Spacer(modifier = Modifier.weight(1f))
        }

        Box {
            ChatAppIconButton(
                onClick = onChatOptionsClick,
                enabled = chatUi != null,
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.dots_icon),
                    contentDescription = stringResource(Res.string.open_chat_options_menu),
                    modifier = Modifier.size(MaterialTheme.iconSize.large),
                    tint = MaterialTheme.colorScheme.extended.textSecondary,
                )
            }

            ChatAppDropdownMenu(
                expanded = isChatOptionsDropDownOpen,
                onDismiss = onDismissChatOptions,
                items = listOf(
                    DropdownItem(
                        title = stringResource(Res.string.chat_members),
                        icon = vectorResource(DesignSystemRes.drawable.users_icon),
                        contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                        onClick = onManageChatClick,
                    ),
                    DropdownItem(
                        title = stringResource(Res.string.leave_chat),
                        icon = vectorResource(DesignSystemRes.drawable.logout_icon),
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onLeaveChatClick,
                    ),
                )
            )
        }
    }
}

@ThemedPreview
@Composable
private fun ChatDetailHeaderContentPreview() {
    ChatAppPreview {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            ChatHeaderContent {
                ChatDetailHeader(
                    isDetailPresent = false,
                    isChatOptionsDropDownOpen = true,
                    chatUi = ChatUi(
                        id = "1",
                        localParticipant = ChatParticipantUi(
                            id = "1",
                            username = "Ivan",
                            initials = "IV ",
                        ),
                        otherParticipants = listOf(
                            ChatParticipantUi(
                                id = "2",
                                username = "Mike",
                                initials = "MI",
                            ),
                            ChatParticipantUi(
                                id = "3",
                                username = "Dan",
                                initials = "DA",
                            )
                        ),
                        latestMessage = ChatMessage(
                            id = "1",
                            chatId = "1",
                            content = "This is a last chat message that was sent by Ivan " +
                                    "and goes over multiple lines to showcase the ellipsis",
                            createdAt = Clock.System.now(),
                            senderId = "1",
                            deliveryStatus = DeliveryStatus.Sent,
                        ),
                        latestMessageSenderUsername = "Ivan",
                    ),
                    onChatOptionsClick = {},
                    onManageChatClick = {},
                    onLeaveChatClick = {},
                    onDismissChatOptions = {},
                    onBackClick = {},
                )
            }
        }
    }
}

@ThemedPreview
@Composable
private fun ChatDetailHeaderEmptyContentPreview() {
    ChatAppPreview {
        Column(
            modifier = Modifier
                .fillMaxSize(),
        ) {
            ChatHeaderContent {
                ChatDetailHeader(
                    isDetailPresent = false,
                    isChatOptionsDropDownOpen = true,
                    chatUi = null,
                    onChatOptionsClick = {},
                    onManageChatClick = {},
                    onLeaveChatClick = {},
                    onDismissChatOptions = {},
                    onBackClick = {},
                )
            }
        }
    }
}
