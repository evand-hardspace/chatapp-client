package com.evandhardspace.chat.presentation.chat_details

import com.evandhardspace.chat.presentation.model.MessageUi

internal sealed interface ChatDetailsAction {
    data object SendMessage : ChatDetailsAction
    data object ScrollToTop : ChatDetailsAction
    data class SelectChat(val chatId: String?) : ChatDetailsAction
    data class DeleteMessage(val message: MessageUi.LocalUserMessage) : ChatDetailsAction
    data class MessageLongClick(val message: MessageUi.LocalUserMessage) : ChatDetailsAction
    data object DismissMessageMenu : ChatDetailsAction
    data class RetrySendMessage(val message: MessageUi.LocalUserMessage) : ChatDetailsAction
    data object ShowChatOptions : ChatDetailsAction
    data object ChatMembersSelected : ChatDetailsAction
    data object LeaveChat : ChatDetailsAction
    data object DismissChatOptions : ChatDetailsAction
    data class FirstVisibleIndexChanged(val index: Int) : ChatDetailsAction
    data object RetryPagination : ChatDetailsAction
}
