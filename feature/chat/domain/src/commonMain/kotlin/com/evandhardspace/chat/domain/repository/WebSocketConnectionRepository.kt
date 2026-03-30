package com.evandhardspace.chat.domain.repository

import com.evandhardspace.chat.domain.error.ConnectionError
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.ConnectionState
import com.evandhardspace.core.domain.util.EmptyEither
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface ChatConnectionRepository {
    val chatMessages: Flow<ChatMessage>
    val connectionState: StateFlow<ConnectionState>

    suspend fun sendChatMessage(message: ChatMessage): EmptyEither<ConnectionError>
}
