package com.evandhardspace.chat.domain.repository

import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither

interface MessageRepository {
    suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: DeliveryStatus,
    ): EmptyEither<DataError.Local>
}
