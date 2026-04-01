package com.evandhardspace.chat.domain.repository

import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.domain.model.MessageWithSender
import com.evandhardspace.chat.domain.model.OutgoingNewMessage
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither
import kotlinx.coroutines.flow.Flow

interface MessageRepository {
    suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: DeliveryStatus,
    ): EmptyEither<DataError.Local>

    suspend fun fetchMessages(
        chatId: String,
        before: String? = null,
    ): Either<DataError, List<ChatMessage>>

    fun getMessagesForChat(chatId: String): Flow<List<MessageWithSender>>

    suspend fun sendMessage(message: OutgoingNewMessage): EmptyEither<DataError>
}
