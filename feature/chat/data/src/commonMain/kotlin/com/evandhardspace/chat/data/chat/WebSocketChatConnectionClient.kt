package com.evandhardspace.chat.data.chat

import com.evandhardspace.chat.data.dto.websocket.IncomingWebSocketDto
import com.evandhardspace.chat.data.dto.websocket.IncomingWebSocketType
import com.evandhardspace.chat.data.dto.websocket.WebSocketMessageDto
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.data.mapper.toEntity
import com.evandhardspace.chat.data.mapper.toNewMessage
import com.evandhardspace.chat.data.network.WebSocketConnector
import com.evandhardspace.chat.database.ChatAppDatabase
import com.evandhardspace.chat.domain.error.ConnectionError
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.domain.repository.MessageRepository
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.onFailure
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.shareIn
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Single

@Single
internal class WebSocketChatConnectionClient(
    private val webSocketConnector: WebSocketConnector,
    private val chatRepository: ChatRepository,
    private val database: ChatAppDatabase,
    private val sessionRepository: MutableSessionRepository,
    private val json: Json,
    private val messageRepository: MessageRepository,
    private val applicationScope: CoroutineScope,
) {

    val chatMessages: Flow<ChatMessage> = webSocketConnector
        .messages
        .mapNotNull(::parseIncomingMessage)
        .onEach(::handleIncomingMessage)
        .filterIsInstance<IncomingWebSocketDto.NewMessageDto>()
        .mapNotNull {
            database.chatMessageDao.getMessageById(it.id)?.toDomain()
        }
        .shareIn(
            scope = applicationScope,
            started = SharingStarted.WhileSubscribed(5000),
        )

    val connectionState = webSocketConnector.connectionState

    suspend fun sendChatMessage(message: ChatMessage): EmptyEither<ConnectionError> {
        val outgoingDto = message.toNewMessage()
        val webSocketMessage = WebSocketMessageDto(
            type = outgoingDto.type.name,
            payload = json.encodeToString(outgoingDto),
        )
        val rawJsonPayload = json.encodeToString(webSocketMessage)

        return webSocketConnector
            .sendMessage(rawJsonPayload)
            .onFailure {
                messageRepository.updateMessageDeliveryStatus(
                    messageId = message.id,
                    status = DeliveryStatus.Failed,
                )
            }
    }

    private fun parseIncomingMessage(message: WebSocketMessageDto): IncomingWebSocketDto? {
        val deserializer = when (message.type) {
            IncomingWebSocketType.NewMessage.name ->
                IncomingWebSocketDto.NewMessageDto.serializer()

            IncomingWebSocketType.MessageDeleted.name ->
                IncomingWebSocketDto.MessageDeletedDto.serializer()

            IncomingWebSocketType.ProfilePictureUpdated.name ->
                IncomingWebSocketDto.ProfilePictureUpdated.serializer()

            IncomingWebSocketType.ChatParticipantsChanged.name ->
                IncomingWebSocketDto.ChatParticipantsChangedDto.serializer()

            else -> return null
        }
        return json.decodeFromString(deserializer, message.payload)
    }

    private suspend fun handleIncomingMessage(message: IncomingWebSocketDto) {
        when (message) {
            is IncomingWebSocketDto.ChatParticipantsChangedDto -> refreshChat(message)
            is IncomingWebSocketDto.MessageDeletedDto -> deleteMessage(message)
            is IncomingWebSocketDto.NewMessageDto -> handleNewMessage(message)
            is IncomingWebSocketDto.ProfilePictureUpdated -> updateProfilePicture(message)
        }
    }

    private suspend fun refreshChat(message: IncomingWebSocketDto.ChatParticipantsChangedDto) {
        chatRepository.fetchChatById(message.chatId)
    }

    private suspend fun deleteMessage(message: IncomingWebSocketDto.MessageDeletedDto) {
        database.chatMessageDao.deleteMessageById(message.messageId)
    }

    private suspend fun handleNewMessage(message: IncomingWebSocketDto.NewMessageDto) {
        val chatExists = database.chatDao.getChatById(message.chatId) != null
        if (chatExists.not()) {
            chatRepository.fetchChatById(message.chatId)
        }

        val entity = message.toEntity()
        database.chatMessageDao.upsertMessage(entity)
    }

    private suspend fun updateProfilePicture(message: IncomingWebSocketDto.ProfilePictureUpdated) {
        database.chatParticipantDao.updateProfilePictureUrl(
            userId = message.userId,
            newUrl = message.newUrl,
        )

        val authState = sessionRepository.authState.firstOrNull() as? AuthState.Authenticated
        if (authState != null) {
            sessionRepository.saveAuthState(
                info = authState.copy(
                    user = authState.user.copy(
                        profilePictureUrl = message.newUrl,
                    )
                )
            )
        }
    }
}
