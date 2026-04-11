package com.evandhardspace.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class IncomingWebSocketType(val value: String) {
    NewMessage("NEW_MESSAGE"),
    MessageDeleted("MESSAGE_DELETED"),
    ProfilePictureUpdated("PROFILE_PICTURE_UPDATED"),
    ChatParticipantsChanged("CHAT_PARTICIPANTS_CHANGED"),
}

@Serializable
sealed interface IncomingWebSocketDto {
    @Serializable
    data class NewMessageDto(
        val id: String,
        val chatId: String,
        val content: String,
        val senderId: String,
        val createdAt: String,
        val type: IncomingWebSocketType = IncomingWebSocketType.NewMessage,
    ) : IncomingWebSocketDto

    @Serializable
    data class MessageDeletedDto(
        val messageId: String,
        val chatId: String,
        val type: IncomingWebSocketType = IncomingWebSocketType.MessageDeleted,
    ) : IncomingWebSocketDto

    @Serializable
    data class ProfilePictureUpdated(
        val userId: String,
        val newUrl: String?,
        val type: IncomingWebSocketType = IncomingWebSocketType.ProfilePictureUpdated,
    ) : IncomingWebSocketDto

    @Serializable
    data class ChatParticipantsChangedDto(
        val chatId: String,
        val type: IncomingWebSocketType = IncomingWebSocketType.ChatParticipantsChanged,
    ) : IncomingWebSocketDto
}
