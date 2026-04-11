package com.evandhardspace.chat.presentation.chat_list

internal sealed interface ChatListAction {
    data object OpenUserMenu: ChatListAction
    data object DismissUserMenu: ChatListAction
    data object OpenLogout: ChatListAction
    data object Logout: ChatListAction
    data object DismissLogoutDialog: ChatListAction
    data class SelectChat(val chatId: String?): ChatListAction
}
