package com.evandhardspace.chat.presentation.mapper

import com.evandhardspace.chat.domain.model.MessageWithSender
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.presentation.util.datetime.formatMessageTime

internal fun MessageWithSender.toUi(
    localUserId: String,
): MessageUi {
    val isFromLocalUser = this.sender.userId == localUserId
    return if(isFromLocalUser) {
        MessageUi.LocalUserMessage(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
            isMenuOpen = false,
            formattedSentTime = message.createdAt.formatMessageTime(),
        )
    } else {
        MessageUi.OtherUserMessage(
            id = message.id,
            content = message.content,
            formattedSentTime = message.createdAt.formatMessageTime(),
            sender = sender.toUi(),
        )
    }
}
