package com.evandhardspace.chat.data.chat

import com.evandhardspace.chat.data.dto.websocket.WebSocketMessageDto
import com.evandhardspace.chat.data.mapper.toNewMessage
import com.evandhardspace.chat.data.network.WebSocketConnector
import com.evandhardspace.chat.database.ChatAppDatabase
import com.evandhardspace.chat.domain.error.ConnectionError
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.domain.repository.MessageRepository
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.onFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.json.Json

internal class WebSocketChatConnectionClient(
    private val webSocketConnector: WebSocketConnector,
    private val chatRepository: ChatRepository,
    private val database: ChatAppDatabase,
    private val sessionStorage: SessionRepository,
    private val json: Json,
    private val messageRepository: MessageRepository,
) {

    val chatMessages: Flow<ChatMessage>
        get() = TODO("Not yet implemented")

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
            .onFailure { error ->
                messageRepository.updateMessageDeliveryStatus(
                    messageId = message.id,
                    status = DeliveryStatus.Failed,
                )
            }
    }
}
