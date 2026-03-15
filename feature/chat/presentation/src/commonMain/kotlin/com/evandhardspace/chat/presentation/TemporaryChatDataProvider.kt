package com.evandhardspace.chat.presentation

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.presentation.chat_details.ChatDetailsState
import com.evandhardspace.chat.presentation.chat_list.ChatListState
import com.evandhardspace.chat.presentation.model.ChatUi
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.presentation.util.asUiText
import kotlin.time.Clock
import kotlin.uuid.Uuid

internal fun temporaryChatListState(): ChatListState = ChatListState(
    chats = buildList {
        repeat(100) { chatId ->
            add(
                ChatUi(
                    id = chatId.toString(),
                    localParticipant = ChatParticipantUi(
                        id = "-1",
                        username = "Ivan",
                        initials = "IV",
                        imageUrl = null,
                    ),
                    otherParticipants = buildList {
                        repeat(3) { participantNumber ->
                            add(
                                ChatParticipantUi(
                                    id = (participantNumber + 10_000).toString(),
                                    username = "Other $participantNumber",
                                    initials = "IV",
                                    imageUrl = null,
                                )
                            )
                        }
                    },
                    latestMessage = ChatMessage(
                        id = Uuid.random().toString(),
                        chatId = chatId.toString(),
                        content = "Some message",
                        createdAt = Clock.System.now(),
                        senderId = "2",
                    ),
                    latestMessageSenderUsername = "IV",
                )
            )
        }
    },
)

internal fun temporaryChatDetailsState(): ChatDetailsState = ChatDetailsState(
    messageTextFieldState = TextFieldState(
        initialText = "This is a new message!",
    ),
    canSendMessage = true,
    chatUi = ChatUi(
        id = "1",
        localParticipant = ChatParticipantUi(
            id = "1",
            username = "Ivan",
            initials = "IV",
        ),
        otherParticipants = listOf(
            ChatParticipantUi(
                id = "2",
                username = "John",
                initials = "JO",
            ),
            ChatParticipantUi(
                id = "3",
                username = "Dan",
                initials = "DA",
            ),
        ),
        latestMessage = ChatMessage(
            id = "1",
            chatId = "1",
            content = "This is a last chat message that was sent by Philipp " +
                    "and goes over multiple lines to showcase the ellipsis",
            createdAt = Clock.System.now(),
            senderId = "1",
        ),
        latestMessageSenderUsername = "IV",
    ),
    messages = (1..20).map {
        if (it % 2 == 0) {
            MessageUi.LocalUserMessage(
                id = it.toString(),
                content = "Hello world!",
                deliveryStatus = DeliveryStatus.Sent,
                isMenuOpen = false,
                formattedSentTime = "Friday, Aug 20".asUiText(),
            )
        } else {
            MessageUi.OtherUserMessage(
                id = it.toString(),
                content = "Hello world!",
                sender = ChatParticipantUi(
                    id = Uuid.random().toString(),
                    username = "John",
                    initials = "JO",
                ),
                formattedSentTime = "Friday, Aug 20".asUiText(),
            )
        }
    }
)
