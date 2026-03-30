package com.evandhardspace.chat.data.datasource

import com.evandhardspace.chat.data.dto.ChatDto
import com.evandhardspace.chat.data.dto.request.CreateChatRequest
import com.evandhardspace.chat.data.dto.request.ParticipantsRequest
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.core.data.networking.delete
import com.evandhardspace.core.data.networking.get
import com.evandhardspace.core.data.networking.post
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.asEmptyEither
import com.evandhardspace.core.domain.util.map
import io.ktor.client.HttpClient
import org.koin.core.annotation.Factory

@Factory
internal class ChatRemoteDataSource(
    private val httpClient: HttpClient,
) {

    suspend fun createChat(otherUserIds: List<String>): Either<DataError.Remote, Chat> =
        httpClient.post<CreateChatRequest, ChatDto>(
            route = "/chat",
            body = CreateChatRequest(
                otherUserIds = otherUserIds,
            ),
        ).map(ChatDto::toDomain)

    suspend fun getChats(): Either<DataError.Remote, List<Chat>> =
        httpClient.get<List<ChatDto>>(
            route = "/chat",
        ).map { chatDtos ->
            chatDtos.map(ChatDto::toDomain)
        }

    suspend fun getChatById(chatId: String): Either<DataError.Remote, Chat> =
        httpClient.get<ChatDto>(
            route = "/chat/$chatId",
        ).map(ChatDto::toDomain)

    suspend fun leaveChat(chatId: String): EmptyEither<DataError.Remote> =
        httpClient.delete<Unit>(
            route = "/chat/$chatId/leave",
        ).asEmptyEither()

    suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>,
    ): Either<DataError.Remote, Chat> = httpClient.post<ParticipantsRequest, ChatDto>(
        route = "/chat/$chatId/add",
        body = ParticipantsRequest(
            userIds = userIds,
        ),
    ).map(ChatDto::toDomain)
}