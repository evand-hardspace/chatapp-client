package com.evandhardspace.chat.data.dto.websocket

import kotlinx.serialization.Serializable

enum class OutgoingWebSocketType {
    NewMessage,
}

@Serializable
sealed class OutgoingWebSocketDto(
    val type: OutgoingWebSocketType,
) {

    @Serializable
    data class NewMessage(
        val chatId: String,
        val messageId: String,
        val content: String,
    ): OutgoingWebSocketDto(OutgoingWebSocketType.NewMessage)
}
