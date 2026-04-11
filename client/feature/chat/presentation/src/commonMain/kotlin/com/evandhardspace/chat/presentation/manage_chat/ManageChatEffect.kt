package com.evandhardspace.chat.presentation.manage_chat

sealed interface ManageChatEffect {
    data object MembersAdded: ManageChatEffect
}
