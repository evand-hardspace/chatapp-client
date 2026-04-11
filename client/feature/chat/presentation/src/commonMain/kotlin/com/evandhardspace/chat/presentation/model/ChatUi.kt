package com.evandhardspace.chat.presentation.model

import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi

data class ChatUi(
    val id: String,
    val localParticipant: ChatParticipantUi,
    val otherParticipants: List<ChatParticipantUi>,
    val latestMessage: ChatMessage?,
    val latestMessageSenderUsername: String?,
) {
    val isGroupChat = otherParticipants.size > 1
}
