package com.evandhardspace.chat.domain.repository

import com.evandhardspace.chat.domain.Clearable
import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.chat.domain.model.ChatInfo
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither
import kotlinx.coroutines.flow.Flow

interface ChatRepository: Clearable {
    val chats: Flow<List<Chat>>

    suspend fun fetchChats(): Either<DataError.Remote, List<Chat>>

    fun getActiveParticipantsByChatId(
        chatId: String,
    ): Flow<List<ChatParticipant>>

    suspend fun createChat(
        otherUserIds: List<String>,
    ): Either<DataError.Remote, Chat>

    fun getChatInfoById(
        chatId: String,
    ): Flow<ChatInfo>

    suspend fun fetchChatById(
        chatId: String,
    ): EmptyEither<DataError.Remote>

    suspend fun leaveChat(
        chatId: String,
    ): EmptyEither<DataError.Remote>

    suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>,
    ): Either<DataError.Remote, Chat>
}
