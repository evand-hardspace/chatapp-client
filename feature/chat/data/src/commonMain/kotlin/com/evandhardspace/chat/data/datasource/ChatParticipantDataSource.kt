package com.evandhardspace.chat.data.datasource

import com.evandhardspace.chat.data.dto.ChatParticipantDto
import com.evandhardspace.chat.data.dto.request.ConfirmProfilePictureRequest
import com.evandhardspace.chat.data.dto.response.ProfilePictureUploadUrlsResponse
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.chat.domain.model.ProfilePictureUploadUrls
import com.evandhardspace.core.data.networking.get
import com.evandhardspace.core.data.networking.post
import com.evandhardspace.core.data.networking.put
import com.evandhardspace.core.data.networking.safeCall
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.map
import io.ktor.client.HttpClient
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.put
import io.ktor.client.request.setBody
import io.ktor.client.request.url
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
            ),
        ).map { it.toDomain() }

    suspend fun getLocalParticipant(): Either<DataError.Remote, ChatParticipant> =
        httpClient.get<ChatParticipantDto>(
            route = "/participants",
        ).map { it.toDomain() }

    suspend fun getProfilePictureUploadUrl(mimeType: String): Either<DataError.Remote, ProfilePictureUploadUrls> =
        httpClient.post<Unit, ProfilePictureUploadUrlsResponse>(
            route = "/participants/profile-picture-upload",
            queryParams = mapOf(
                "mimeType" to mimeType,
            ),
            body = Unit,
        ).map { it.toDomain() }

    suspend fun uploadProfilePicture(
        uploadUrl: String,
        imageBytes: ByteArray,
        headers: Map<String, String>,
    ): EmptyEither<DataError.Remote> = safeCall {
        httpClient.put {
            url(uploadUrl)
            headers.forEach { (key, value) ->
                header(key, value)
            }
            setBody(imageBytes)
        }
    }

    suspend fun confirmProfilePictureUpload(publicUrl: String): EmptyEither<DataError.Remote> =
        httpClient.post<ConfirmProfilePictureRequest, Unit>(
            route = "/participants/confirm-profile-picture",
            body = ConfirmProfilePictureRequest(publicUrl),
        )
}
