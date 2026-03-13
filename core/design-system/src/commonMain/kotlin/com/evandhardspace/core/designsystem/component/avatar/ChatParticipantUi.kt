package com.evandhardspace.core.designsystem.component.avatar

data class ChatParticipantUi(
    val id: String,
    val username: String,
    val initials: String,
    val imageUrl: String? = null,
)
