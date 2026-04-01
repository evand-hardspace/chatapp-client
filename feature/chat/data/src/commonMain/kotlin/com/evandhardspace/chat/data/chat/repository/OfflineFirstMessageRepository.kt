package com.evandhardspace.chat.data.chat.repository

import com.evandhardspace.chat.data.chat.constant.ChatMessageConstants
import com.evandhardspace.chat.data.database.safeDatabaseUpdate
import com.evandhardspace.chat.data.datasource.ChatMessageDataSource
import com.evandhardspace.chat.data.dto.websocket.OutgoingWebSocketDto
import com.evandhardspace.chat.data.dto.websocket.WebSocketMessageDto
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.data.mapper.toEntity
import com.evandhardspace.chat.data.mapper.toWebSocketDto
import com.evandhardspace.chat.data.network.WebSocketConnector
import com.evandhardspace.chat.database.ChatAppDatabase
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.domain.model.MessageWithSender
import com.evandhardspace.chat.domain.model.OutgoingNewMessage
import com.evandhardspace.chat.domain.repository.MessageRepository
import com.evandhardspace.core.common.di.ApplicationScope
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.asFailure
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single
import kotlin.time.Clock

@Single
internal class OfflineFirstMessageRepository(
    private val database: ChatAppDatabase,
    private val chatMessageDataSource: ChatMessageDataSource,
    private val sessionRepository: SessionRepository,
    private val webSocketConnector: WebSocketConnector,
    private val json: Json,
    @param:ApplicationScope private val applicationScope: CoroutineScope,
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

    override suspend fun sendMessage(message: OutgoingNewMessage): EmptyEither<DataError> {
        return safeDatabaseUpdate {
            val dto = message.toWebSocketDto()

            val localUser = (sessionRepository.authState.first() as? AuthState.Authenticated)?.user
                ?: return DataError.Local.NotFound.asFailure()

            val entity = dto.toEntity(
                senderId = localUser.id,
                deliveryStatus = DeliveryStatus.Sending,
            )
            database.chatMessageDao.upsertMessage(entity)

            return webSocketConnector
                .sendMessage(dto.toJsonPayload())
                .onFailure {
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatus(
                            messageId = entity.messageId,
                            status = DeliveryStatus.Failed.name,
                            timestamp = Clock.System.now().toEpochMilliseconds(),
                        )
                    }.join()
                }
        }
    }

    override suspend fun retryMessage(messageId: String): EmptyEither<DataError> {
        return safeDatabaseUpdate {
            println("Message ID retry $messageId")
            val message = database.chatMessageDao.getMessageById(messageId)
                ?: return DataError.Local.NotFound.asFailure()

            database.chatMessageDao.updateDeliveryStatus(
                messageId = messageId,
                timestamp = Clock.System.now().toEpochMilliseconds(),
                status = DeliveryStatus.Sending.name,
            )

            val outgoingNewMessage = OutgoingWebSocketDto.NewMessage(
                chatId = message.chatId,
                messageId = messageId,
                content = message.content,
            )
            return webSocketConnector
                .sendMessage(outgoingNewMessage.toJsonPayload())
                .onFailure {
                    applicationScope.launch {
                        database.chatMessageDao.updateDeliveryStatus(
                            messageId = messageId,
                            status = DeliveryStatus.Failed.name,
                            timestamp = Clock.System.now().toEpochMilliseconds(),
                        )
                    }.join()
                }
        }
    }

    private fun OutgoingWebSocketDto.NewMessage.toJsonPayload(): String {
        val webSocketMessage = WebSocketMessageDto(
            type = type.value,
            payload = json.encodeToString(this),
        )
        return json.encodeToString(webSocketMessage)
    }
}
