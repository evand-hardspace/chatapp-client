package com.evandhardspace.chat.domain.repository

import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither

interface ChatParticipantRepository {
    suspend fun searchParticipant(
        query: String,
    ): Either<DataError.Remote, ChatParticipant>

    suspend fun fetchLocalParticipant(): Either<DataError, ChatParticipant>

    suspend fun uploadProfilePicture(
        imageBytes: ByteArray,
        mimeType: String,
    ): EmptyEither<DataError.Remote>
}
