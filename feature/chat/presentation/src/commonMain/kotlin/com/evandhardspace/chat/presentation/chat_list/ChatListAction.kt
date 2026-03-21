package com.evandhardspace.chat.presentation.chat_list

import com.evandhardspace.chat.presentation.model.ChatUi

internal sealed interface ChatListAction {
    data object OnUserAvatar: ChatListAction
    data object OnDismissUserMenu: ChatListAction
    data object OnLogout: ChatListAction
    data object OnDismissLogoutDialog: ChatListAction
    data class OnSelectChat(val chat: ChatUi?): ChatListAction
}
