package com.evandhardspace.chat.domain

data class MessageWithSender(
    val message: ChatMessage,
    val sender: ChatParticipant,
    val deliveryStatus: DeliveryStatus?,
)
