package com.evandhardspace.chat.presentation.mapper

import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi

fun ChatParticipant.toUi(): ChatParticipantUi = ChatParticipantUi(
    id = userId,
    username = username,
    initials = initials,
    imageUrl = profilePictureUrl,
)
