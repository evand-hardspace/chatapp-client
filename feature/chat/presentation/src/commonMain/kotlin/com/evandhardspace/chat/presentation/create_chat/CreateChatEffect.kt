package com.evandhardspace.chat.presentation.create_chat

sealed interface CreateChatEffect {
    data class OnChatCreated(val chatId: String) : CreateChatEffect
}
