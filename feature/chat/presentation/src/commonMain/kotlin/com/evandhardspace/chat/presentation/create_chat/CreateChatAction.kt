package com.evandhardspace.chat.presentation.create_chat

sealed interface CreateChatAction {
    data object OnAdd: CreateChatAction
    data object OnCreateChat: CreateChatAction
}
