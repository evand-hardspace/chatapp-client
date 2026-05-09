package com.evandhardspace.chat.data.chat.repository

import com.evandhardspace.chat.data.datasource.ChatParticipantDataSource
import com.evandhardspace.chat.domain.repository.ChatParticipantRepository
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.onSuccess
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesBinding
import dev.zacsweers.metro.Inject

@ContributesBinding(AppScope::class)
@Inject
class DefaultChatParticipantRepository(
    private val chatParticipantDataSource: ChatParticipantDataSource,
    private val sessionRepository: SessionRepository,
) : ChatParticipantRepository {

    override suspend fun searchParticipant(query: String): Either<DataError.Remote, ChatParticipant> =
        chatParticipantDataSource.searchParticipant(query)

    override suspend fun fetchLocalParticipant(): Either<DataError, ChatParticipant> =
        chatParticipantDataSource.getLocalParticipant()
            .onSuccess { participant ->
                sessionRepository.updateAuthState { authState ->
                    authState.copy(
                        user = authState.user.copy(
                            id = participant.userId,
                            username = participant.username,
                            profilePictureUrl = participant.profilePictureUrl,
                        ),
                    )
                }
            }

    override suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String
    ): EmptyEither<DataError.Remote> {
        val result = chatParticipantDataSource.getProfilePictureUploadUrl(mimeType)

        if (result is Either.Failure) return result

        val uploadUrls = (result as Either.Success).data
        val uploadResult = chatParticipantDataSource.uploadProfilePicture(
            uploadUrl = uploadUrls.uploadUrl,
            imageBytes = imageBytes,
            headers = uploadUrls.headers,
        )

        if (uploadResult is Either.Failure) return uploadResult

        return chatParticipantDataSource
            .confirmProfilePictureUpload(uploadUrls.publicUrl)
            .onSuccess {
                sessionRepository.updateAuthState { authState ->
                    authState.copy(
                        user = authState.user.copy(
                            profilePictureUrl = uploadUrls.publicUrl,
                        ),
                    )
                }
            }
    }

    override suspend fun deleteProfilePicture(): EmptyEither<DataError.Remote> =
        chatParticipantDataSource.deleteProfilePicture()
            .onSuccess {
                sessionRepository.updateAuthState { authState ->
                    authState.copy(
                        user = authState.user.copy(
                            profilePictureUrl = null,
                        ),
                    )
                }
            }

}
