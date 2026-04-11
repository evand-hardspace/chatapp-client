package com.evandhardspace.chat.presentation.mapper

import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.domain.auth.User

fun ChatParticipant.toUi(): ChatParticipantUi = ChatParticipantUi(
    id = userId,
    username = username,
    initials = initials,
    imageUrl = profilePictureUrl,
)

fun User.toUi(): ChatParticipantUi = ChatParticipantUi(
    id = id,
    username = username,
    initials = username.take(2).uppercase(),
    imageUrl = profilePictureUrl,
)