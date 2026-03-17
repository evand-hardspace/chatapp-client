package com.evandhardspace.chat.domain

import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.chat.domain.model.ChatInfo
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither
import kotlinx.coroutines.flow.Flow

interface ChatRepository {
    val chats: Flow<List<Chat>>

    suspend fun fetchChats(): Either<DataError.Remote, List<Chat>>

    suspend fun createChat(
        otherUserIds: List<String>,
    ): Either<DataError.Remote, Chat>

    fun getChatInfoById(
        chatId: String,
    ): Flow<ChatInfo>

    suspend fun fetchChatById(
        chatId: String,
    ): EmptyEither<DataError.Remote>
}
