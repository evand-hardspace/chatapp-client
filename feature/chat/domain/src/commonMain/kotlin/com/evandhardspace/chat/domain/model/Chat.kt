package com.evandhardspace.chat.domain.model

import kotlin.time.Instant

data class Chat(
    val id: String,
    val participants: List<ChatParticipant>,
    val latestActivityAt: Instant,
    val latestMessage: ChatMessage?,
)
