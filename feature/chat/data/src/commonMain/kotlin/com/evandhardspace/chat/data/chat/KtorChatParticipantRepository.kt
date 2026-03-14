package com.evandhardspace.chat.data.chat

import com.evandhardspace.chat.domain.ChatParticipantRepository
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.data.networking.get
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Result
import com.evandhardspace.core.domain.util.map
import com.evandhardspace.chat.data.dto.ChatParticipantDto
import com.evandhardspace.chat.data.mapper.toDomain
import io.ktor.client.HttpClient
import org.koin.core.annotation.Factory

@Factory
class KtorChatParticipantRepository(
    private val httpClient: HttpClient,
) : ChatParticipantRepository {

    override suspend fun searchParticipant(query: String): Result<DataError.Remote, ChatParticipant> =
        httpClient.get<ChatParticipantDto>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query,
            )
        ).map { it.toDomain() }
}
