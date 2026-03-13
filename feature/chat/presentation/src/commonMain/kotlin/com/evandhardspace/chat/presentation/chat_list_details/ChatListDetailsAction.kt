package com.evandhardspace.chat.presentation.chat_list_details

sealed interface ChatListDetailsAction {
    data class OnOpenChat(val chatId: String?): ChatListDetailsAction
    data object OnOpenProfileSettings: ChatListDetailsAction
    data object OnCreateChat: ChatListDetailsAction
    data object OnManageChat: ChatListDetailsAction
    data object OnDismissCurrentDialog: ChatListDetailsAction
}
