package com.evandhardspace.chat.presentation.component.manage_chat

import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi

sealed interface ManageChatAction {
    data object Submit : ManageChatAction
    data class RemoveSelectedParticipant(val participant: ChatParticipantUi) : ManageChatAction
    data class SelectParticipant(val participant: ChatParticipantUi) : ManageChatAction
    data class SelectChat(val chatId: String?) : ManageChatAction
}
