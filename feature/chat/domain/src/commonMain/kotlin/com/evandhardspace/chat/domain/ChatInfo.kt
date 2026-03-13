package com.evandhardspace.chat.domain

data class ChatInfo(
    val chat: Chat,
    val messages: List<MessageWithSender>,
)
