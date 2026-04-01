package com.evandhardspace.chat.presentation.mapper

import com.evandhardspace.chat.domain.model.MessageWithSender
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.presentation.util.datetime.formatDateSeparator
import com.evandhardspace.core.presentation.util.datetime.formatMessageTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun List<MessageWithSender>.toUiList(
    localUserId: String,
    timeZone: TimeZone = TimeZone.currentSystemDefault(),
): List<MessageUi> =
    sortedByDescending { it.message.createdAt }
        .groupBy { it.message.createdAt.toLocalDateTime(timeZone).date }
        .flatMap { (date, messages) ->
            messages.map { it.toUi(localUserId) } + MessageUi.DateSeparator(
                id = date.toString(),
                date = date.formatDateSeparator(),
            )
        }

internal fun MessageWithSender.toUi(
    localUserId: String,
): MessageUi {
    val isFromLocalUser = this.sender.userId == localUserId
    return if (isFromLocalUser) {
        MessageUi.LocalUserMessage(
            id = message.id,
            content = message.content,
            deliveryStatus = message.deliveryStatus,
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
