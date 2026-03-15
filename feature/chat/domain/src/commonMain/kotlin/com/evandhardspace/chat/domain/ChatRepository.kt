package com.evandhardspace.chat.domain

import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either

interface ChatRepository {
    suspend fun createChat(
        otherUserIds: List<String>,
    ): Either<DataError.Remote, Chat>
}
