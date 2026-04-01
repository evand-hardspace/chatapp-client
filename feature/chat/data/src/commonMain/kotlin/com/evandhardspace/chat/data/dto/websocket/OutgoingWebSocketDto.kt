package com.evandhardspace.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingWebSocketType(val value: String) {
    NewMessage("NEW_MESSAGE"),
}

@Serializable
sealed interface OutgoingWebSocketDto {
    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String,
        val type: OutgoingWebSocketType = OutgoingWebSocketType.NewMessage,
    ) : OutgoingWebSocketDto
}
