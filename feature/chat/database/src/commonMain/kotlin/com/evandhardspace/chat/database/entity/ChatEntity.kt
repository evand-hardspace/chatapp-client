package com.evandhardspace.chat.database.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ChatEntity(
    @PrimaryKey
    val chatId: String,
    val latestMessage: String?,
    val latestActivityAt: Long,
)
