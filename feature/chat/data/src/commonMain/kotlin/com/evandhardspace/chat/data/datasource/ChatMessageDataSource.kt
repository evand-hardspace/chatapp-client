package com.evandhardspace.chat.data.datasource

import com.evandhardspace.chat.data.chat.constant.ChatMessageConstants
import com.evandhardspace.chat.data.dto.ChatMessageDto
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.core.data.networking.get
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.map
import io.ktor.client.HttpClient
import org.koin.core.annotation.Single

@Single
internal class ChatMessageDataSource(
    private val httpClient: HttpClient,
) {

    suspend fun fetchMessages(
        chatId: String,
        before: String?,
    ): Either<DataError.Remote, List<ChatMessage>> {
        return httpClient.get<List<ChatMessageDto>>(
            route = "/chat/$chatId/messages",
            queryParams = buildMap {
                this["pageSize"] = ChatMessageConstants.PageSize
                if (before != null) {
                    this["before"] = before
                }
            }
        ).map { chatMessages ->
            chatMessages.map { message ->
                message.toDomain()
            }
        }
    }
}
