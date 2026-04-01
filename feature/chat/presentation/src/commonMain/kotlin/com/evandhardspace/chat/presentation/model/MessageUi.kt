package com.evandhardspace.chat.presentation.model

import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.presentation.util.UiText

sealed interface MessageUi {

    val id: String

    data class LocalUserMessage(
        override val id: String,
        val content: String,
        val deliveryStatus: DeliveryStatus,
        val formattedSentTime: UiText,
    ): MessageUi

    data class OtherUserMessage(
        override val id: String,
        val content: String,
        val formattedSentTime: UiText,
        val sender: ChatParticipantUi,
    ): MessageUi

    data class DateSeparator(
        override val id: String,
        val date: UiText,
        val isToday: Boolean,
    ): MessageUi
}
