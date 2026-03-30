package com.evandhardspace.chat.data.chat.repository

import com.evandhardspace.chat.data.chat.constant.ChatMessageConstants
import com.evandhardspace.chat.data.database.safeDatabaseUpdate
import com.evandhardspace.chat.data.datasource.ChatMessageDataSource
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.data.mapper.toEntity
import com.evandhardspace.chat.database.ChatAppDatabase
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.domain.model.MessageWithSender
import com.evandhardspace.chat.domain.repository.MessageRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Single
import kotlin.time.Clock

@Single
internal class OfflineFirstMessageRepository(
    private val database: ChatAppDatabase,
    private val chatMessageDataSource: ChatMessageDataSource,
) : MessageRepository {

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: DeliveryStatus,
    ): EmptyEither<DataError.Local> = safeDatabaseUpdate {
        database.chatMessageDao.updateDeliveryStatus(
            messageId = messageId,
            status = status.name,
            timestamp = Clock.System.now().toEpochMilliseconds(),
        )
    }

    override suspend fun fetchMessages(
        chatId: String,
        before: String?,
    ): Either<DataError, List<ChatMessage>> {
        return chatMessageDataSource
            .fetchMessages(chatId, before)
            .onSuccess { messages ->
                return safeDatabaseUpdate {
                    database.chatMessageDao.upsertMessages(
                        chatId = chatId,
                        serverMessages = messages.map { it.toEntity() },
                        pageSize = ChatMessageConstants.PageSize,
                        shouldSync = before == null, // Only sync for most recent page
                    )
                    messages
                }
            }
    }

    override fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>> = database
        .chatMessageDao
        .getMessagesByChatId(chatId)
        .map { messages ->
            messages.map { message ->
                message.toDomain()
            }
        }
}