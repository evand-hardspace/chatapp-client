package com.evandhardspace.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingWebSocketType {
    NewMessage,
    MessageDeleted,
    ProfilePictureUpdated,
    ChatParticipantsChanged,
}

@Serializable
sealed class IncomingWebSocketDto(
    val type: IncomingWebSocketType,
) {

    @Serializable
    data class NewMessageDto(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String,
    ): IncomingWebSocketDto(IncomingWebSocketType.NewMessage)

    @Serializable
    data class MessageDeletedDto(
        val messageId: String,
        val chatId: String,
    ): IncomingWebSocketDto(IncomingWebSocketType.MessageDeleted)

    @Serializable
    data class ProfilePictureUpdated(
        val userId: String,
        val newUrl: String?,
    ): IncomingWebSocketDto(IncomingWebSocketType.ProfilePictureUpdated)

    @Serializable
    data class ChatParticipantsChangedDto(
        val chatId: String,
    ): IncomingWebSocketDto(IncomingWebSocketType.ChatParticipantsChanged)
}
