package com.evandhardspace.chat.domain

import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Result


interface ChatParticipantRepository {
    suspend fun searchParticipant(
        query: String,
    ): Result<DataError.Remote, ChatParticipant>
}