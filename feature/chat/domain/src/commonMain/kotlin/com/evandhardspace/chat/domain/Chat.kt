package com.evandhardspace.chat.domain

import kotlin.time.Instant

data class Chat(
    val id: String,
    val participants: List<ChatParticipant>,
    val latestActivityAt: Instant,
    val latestMessage: String?,
)
