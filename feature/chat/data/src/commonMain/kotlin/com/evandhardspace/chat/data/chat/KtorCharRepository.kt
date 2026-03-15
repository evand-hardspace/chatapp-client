package com.evandhardspace.chat.data.chat

import com.evandhardspace.chat.data.dto.ChatDto
import com.evandhardspace.chat.data.dto.request.CreateChatRequest
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.domain.ChatRepository
import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.core.data.networking.post
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.map
import io.ktor.client.HttpClient
import org.koin.core.annotation.Factory

@Factory
class KtorCharRepository(
    private val httpClient: HttpClient,
) : ChatRepository {

    override suspend fun createChat(otherUserIds: List<String>): Either<DataError.Remote, Chat> =
        httpClient.post<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds,
            ),
        ).map { it.toDomain() }
}
