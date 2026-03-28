package com.evandhardspace.chat.domain.model

data class OutgoingNewMessage(
    val chatId: String,
    val messageId: String,
    val content: String,
)
