package com.evandhardspace.chat.presentation.chat_details

import com.evandhardspace.chat.presentation.model.MessageUi

internal sealed interface ChatDetailsAction {
    data object OnSendMessage: ChatDetailsAction
    data object OnScrollToTop: ChatDetailsAction
    data class OnSelectChat(val chatId: String?): ChatDetailsAction
    data class OnDeleteMessage(val message: MessageUi.LocalUserMessage): ChatDetailsAction
    data class OnMessageLongClick(val message: MessageUi.LocalUserMessage): ChatDetailsAction
    data object OnDismissMessageMenu: ChatDetailsAction
    data class OnRetry(val message: MessageUi.LocalUserMessage): ChatDetailsAction
    data object OnChatOptions: ChatDetailsAction
    data object OnChatMembersSelected: ChatDetailsAction
    data object LeaveChat: ChatDetailsAction
    data object OnDismissChatOptions: ChatDetailsAction
}
