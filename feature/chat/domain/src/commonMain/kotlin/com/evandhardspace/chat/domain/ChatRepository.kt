package com.evandhardspace.chat.domain

import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val chats: Flow<List<Chat>>

    suspend fun fetchChats(): Either<DataError.Remote, List<Chat>>

    suspend fun createChat(
        otherUserIds: List<String>,
    ): Either<DataError.Remote, Chat>
}
