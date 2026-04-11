package com.evandhardspace.chat.domain.repository

import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either

interface ChatParticipantRepository {
    suspend fun searchParticipant(
        query: String,
    ): Either<DataError.Remote, ChatParticipant>

    suspend fun fetchLocalParticipant(): Either<DataError, ChatParticipant>
}
