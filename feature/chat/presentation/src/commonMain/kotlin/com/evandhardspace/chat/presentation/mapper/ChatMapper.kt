package com.evandhardspace.chat.presentation.mapper

import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.chat.presentation.model.ChatUi

fun Chat.toUi(localParticipantId: String): ChatUi {
    val (local, other) = participants.partition { it.userId == localParticipantId }
    return ChatUi(
        id = id,
        localParticipant = local.first().toUi(),
        otherParticipants = other.map(ChatParticipant::toUi),
        latestMessage = latestMessage,
        latestMessageSenderUsername = participants
            .find { it.userId == latestMessage?.senderId }
            ?.username,
    )
}
