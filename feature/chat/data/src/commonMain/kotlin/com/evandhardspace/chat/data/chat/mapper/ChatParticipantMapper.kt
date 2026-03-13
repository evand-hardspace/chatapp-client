package com.evandhardspace.chat.data.chat.mapper

import com.evandhardspace.chat.data.chat.dto.ChatParticipantDto
import com.evandhardspace.chat.domain.model.ChatParticipant

fun ChatParticipantDto.toDomain(): ChatParticipant = ChatParticipant(
    userId = userId,
    username = username,
    profilePictureUrl = profilePictureUrl,
)