package com.evandhardspace.chat.data.mapper

import com.evandhardspace.chat.data.dto.ChatDto
import com.evandhardspace.chat.data.dto.ChatMessageDto
import com.evandhardspace.chat.data.dto.ChatParticipantDto
import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.ChatParticipant
import kotlin.time.Instant

fun ChatParticipantDto.toDomain(): ChatParticipant = ChatParticipant(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
)

fun ChatMessageDto.toDomain(): ChatMessage {
    return ChatMessage(
        id = id,
        chatId = chatId,
        content = content,
        createdAt = Instant.parse(createdAt),
        senderId = senderId,
    )
}

fun ChatDto.toDomain(): Chat = Chat(
    id = id,
    participants = participants.map { it.toDomain() },
    latestActivityAt = Instant.parse(lastActivityAt),
    latestMessage = lastMessage?.toDomain(),
)
