package com.evandhardspace.chat.presentation.chat_list_details

sealed interface SharedChatListDetailsAction {
    data class OpenChat(val chatId: String) : SharedChatListDetailsAction
    data object CloseChat : SharedChatListDetailsAction
    data object OpenProfileSettings : SharedChatListDetailsAction
    data object CreateChat : SharedChatListDetailsAction
    data object ManageChat : SharedChatListDetailsAction
    data object DismissCurrentDialog : SharedChatListDetailsAction
}
