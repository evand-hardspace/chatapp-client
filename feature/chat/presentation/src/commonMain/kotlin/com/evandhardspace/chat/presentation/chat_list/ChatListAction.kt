package com.evandhardspace.chat.presentation.chat_list

internal sealed interface ChatListAction {
    data object OnUserAvatar: ChatListAction
    data object OnDismissUserMenu: ChatListAction
    data object OnLogout: ChatListAction
    data object OnDismissLogoutDialog: ChatListAction
}
