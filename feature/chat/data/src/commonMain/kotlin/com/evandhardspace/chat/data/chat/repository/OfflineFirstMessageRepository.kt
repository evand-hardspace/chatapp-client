package com.evandhardspace.chat.data.chat.repository

import com.evandhardspace.chat.data.database.safeDatabaseUpdate
import com.evandhardspace.chat.database.ChatAppDatabase
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.domain.repository.MessageRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.EmptyEither
import org.koin.core.annotation.Single
import kotlin.time.Clock

@Single
class OfflineFirstMessageRepository(
    private val database: ChatAppDatabase,
) : MessageRepository {

    override suspend fun updateMessageDeliveryStatus(
        messageId: String,
        status: DeliveryStatus,
    ): EmptyEither<DataError.Local> = safeDatabaseUpdate {
        database.chatMessageDao.updateDeliveryStatus(
            messageId = messageId,
            status = status.name,
            timestamp = Clock.System.now().toEpochMilliseconds(),
        )
    }
}