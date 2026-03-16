package com.evandhardspace.chat.data.mapper

import com.evandhardspace.chat.data.dto.ChatDto
import com.evandhardspace.chat.data.dto.ChatMessageDto
import com.evandhardspace.chat.data.dto.ChatParticipantDto
import com.evandhardspace.chat.database.entity.ChatEntity
import com.evandhardspace.chat.database.entity.ChatMessageEntity
import com.evandhardspace.chat.database.entity.ChatParticipantEntity
import com.evandhardspace.chat.database.entity.ChatWithParticipants
import com.evandhardspace.chat.database.view.LatestMessageView
import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.chat.domain.model.DeliveryStatus
import kotlin.time.Instant

fun ChatDto.toDomain(): Chat = Chat(
    id = id,
    participants = participants.map { it.toDomain() },
    latestActivityAt = Instant.parse(lastActivityAt),
    latestMessage = lastMessage?.toDomain(),
)

fun Chat.toEntity(): ChatEntity = ChatEntity(
    chatId = id,
    latestActivityAt = latestActivityAt.toEpochMilliseconds(),
)

fun ChatParticipantDto.toDomain(): ChatParticipant = ChatParticipant(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
)

fun ChatWithParticipants.toDomain(): Chat = Chat(
    id = chat.chatId,
    participants = participants.map { it.toDomain() },
    latestActivityAt = Instant.fromEpochMilliseconds(chat.latestActivityAt),
    latestMessage = latestMessage?.toDomain(),
)

fun ChatParticipantEntity.toDomain(): ChatParticipant = ChatParticipant(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
)

fun ChatParticipant.toEntity(): ChatParticipantEntity = ChatParticipantEntity(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
)

fun ChatMessage.toEntity(): ChatMessageEntity = ChatMessageEntity(
    messageId = id,
    chatId = chatId,
    senderId = senderId,
    content = content,
    timestamp = createdAt.toEpochMilliseconds(),
    deliveryStatus = deliveryStatus.name,
)

fun ChatMessage.toLastMessageView(): LatestMessageView = LatestMessageView(
    messageId = id,
    chatId = chatId,
    senderId = senderId,
    content = content,
    timestamp = createdAt.toEpochMilliseconds(),
    deliveryStatus = deliveryStatus.name,
)

fun LatestMessageView.toDomain(): ChatMessage = ChatMessage(
    id = messageId,
    chatId = chatId,
    content = content,
    createdAt = Instant.fromEpochMilliseconds(timestamp),
    senderId = senderId,
    deliveryStatus = DeliveryStatus.valueOf(this.deliveryStatus),
)

fun ChatMessageDto.toDomain(): ChatMessage = ChatMessage(
    id = id,
    chatId = chatId,
    content = content,
    createdAt = Instant.parse(createdAt),
    senderId = senderId,
    deliveryStatus = DeliveryStatus.Sent,
)
