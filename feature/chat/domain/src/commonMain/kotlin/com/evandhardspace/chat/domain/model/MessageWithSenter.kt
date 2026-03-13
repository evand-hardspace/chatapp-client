package com.evandhardspace.chat.domain.model

data class MessageWithSender(
    val message: ChatMessage,
    val sender: ChatParticipant,
    val deliveryStatus: DeliveryStatus?,
)
