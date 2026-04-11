package com.evandhardspace.chat.data.datasource

import com.evandhardspace.chat.data.dto.ChatParticipantDto
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.data.networking.get
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.map
import io.ktor.client.HttpClient
import org.koin.core.annotation.Factory

@Factory
class ChatParticipantDataSource(
    private val httpClient: HttpClient,
) {

    suspend fun searchParticipant(query: String): Either<DataError.Remote, ChatParticipant> =
        httpClient.get<ChatParticipantDto>(
            route = "/participants",
            queryParams = mapOf(
                "query" to query,
            )
        ).map { it.toDomain() }

    suspend fun getLocalParticipant(): Either<DataError.Remote, ChatParticipant> =
        httpClient.get<ChatParticipantDto>(
            route = "/participants",
        ).map { it.toDomain() }
}
