package com.evandhardspace.chat.data.chat

import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.data.mapper.toEntity
import com.evandhardspace.chat.data.mapper.toLastMessageView
import com.evandhardspace.chat.database.ChatAppDatabase
import com.evandhardspace.chat.database.entity.ChatWithParticipants
import com.evandhardspace.chat.domain.ChatRepository
import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.onSuccess
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import org.koin.core.annotation.Factory

@Factory
internal class OfflineFirstChatRepository(
    private val chatDataSource: ChatRemoteDataSource,
    private val database: ChatAppDatabase,
) : ChatRepository {

    override val chats: Flow<List<Chat>>
        get() = database.chatDao.getChatsWithActiveParticipants()
            .map { chatWithParticipantsList ->
                chatWithParticipantsList.map(ChatWithParticipants::toDomain)
            }

    override suspend fun fetchChats(): Either<DataError.Remote, List<Chat>> {
        return chatDataSource
            .getChats()
            .onSuccess { chats ->
                val chatsWithParticipants = chats.map { chat ->
                    ChatWithParticipants(
                        chat = chat.toEntity(),
                        participants = chat.participants.map(ChatParticipant::toEntity),
                        latestMessage = chat.latestMessage?.toLastMessageView(),
                    )
                }

                database.chatDao.upsertChatsWithParticipantsAndCrossRefs(
                    chats = chatsWithParticipants,
                    participantDao = database.chatParticipantDao,
                    crossRefDao = database.chatParticipantsCrossRefDao,
                    chatMessageDao = database.chatMessageDao,
                )
            }
    }

    override suspend fun createChat(otherUserIds: List<String>): Either<DataError.Remote, Chat> =
        chatDataSource.createChat(otherUserIds)
}
