package com.evandhardspace.chat.data.chat.repository

import com.evandhardspace.chat.data.chat.ChatRemoteDataSource
import com.evandhardspace.chat.data.mapper.toDomain
import com.evandhardspace.chat.data.mapper.toEntity
import com.evandhardspace.chat.data.mapper.toLastMessageView
import com.evandhardspace.chat.database.ChatAppDatabase
import com.evandhardspace.chat.database.entity.ChatInfoEntity
import com.evandhardspace.chat.database.entity.ChatParticipantEntity
import com.evandhardspace.chat.database.entity.ChatWithParticipants
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.domain.model.Chat
import com.evandhardspace.chat.domain.model.ChatInfo
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.Either
import com.evandhardspace.core.domain.util.EmptyEither
import com.evandhardspace.core.domain.util.asEmptyEither
import com.evandhardspace.core.domain.util.onSuccess
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.supervisorScope
import org.koin.core.annotation.Factory

@Factory
internal class OfflineFirstChatRepository(
    private val chatDataSource: ChatRemoteDataSource,
    private val database: ChatAppDatabase,
) : ChatRepository {

    override val chats: Flow<List<Chat>>
        get() = database.chatDao.getChatsWithParticipants()
            .map { allChatsWithParticipants ->
                supervisorScope {
                    allChatsWithParticipants
                        .map { chatWithParticipants ->
                            async {
                                ChatWithParticipants(
                                    chat = chatWithParticipants.chat,
                                    participants = chatWithParticipants.participants.filterActive(
                                        chatWithParticipants.chat.chatId
                                    ),
                                    latestMessage = chatWithParticipants.latestMessage,
                                )
                            }
                        }.awaitAll()
                        .map(ChatWithParticipants::toDomain)
                }
            }

    override fun getActiveParticipantsByChatId(
        chatId: String,
    ): Flow<List<ChatParticipant>> = database.chatDao.getActiveParticipantsByChatId(chatId)
        .map { participants ->
            participants.map(ChatParticipantEntity::toDomain)
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
            .onSuccess { chat ->
                upsertChatWithParticipantsAndCrossRefs(chat)
            }

    override fun getChatInfoById(chatId: String): Flow<ChatInfo> =
        database.chatDao.getChatInfoById(chatId)
            .filterNotNull()
            .map { chatInfo ->
                ChatInfoEntity(
                    chat = chatInfo.chat,
                    participants = chatInfo.participants.filterActive(chatId),
                    messagesWithSenders = chatInfo.messagesWithSenders,
                    latestMessage = chatInfo.latestMessage,
                )
            }
            .map(ChatInfoEntity::toDomain)

    override suspend fun fetchChatById(chatId: String): EmptyEither<DataError.Remote> =
        chatDataSource
            .getChatById(chatId)
            .onSuccess { chat ->
                upsertChatWithParticipantsAndCrossRefs(chat)
            }
            .asEmptyEither()

    override suspend fun leaveChat(chatId: String): EmptyEither<DataError.Remote> =
        chatDataSource
            .leaveChat(chatId)
            .onSuccess { database.chatDao.deleteChatById(chatId) }

    private suspend fun upsertChatWithParticipantsAndCrossRefs(chat: Chat) {
        database.chatDao.upsertChatWithParticipantsAndCrossRefs(
            chat = chat.toEntity(),
            participants = chat.participants.map(ChatParticipant::toEntity),
            participantDao = database.chatParticipantDao,
            crossRefDao = database.chatParticipantsCrossRefDao,
        )
    }

    private suspend fun List<ChatParticipantEntity>.filterActive(chatId: String): List<ChatParticipantEntity> {
        val activeParticipantIds = database.chatDao.getActiveParticipantsByChatId(chatId)
            .first()
            .map { it.userId }

        return this.filter { it.userId in activeParticipantIds }
    }

    override suspend fun addParticipantsToChat(
        chatId: String,
        userIds: List<String>,
    ): Either<DataError.Remote, Chat> =
        chatDataSource.addParticipantsToChat(
            chatId = chatId,
            userIds = userIds,
        ).onSuccess { chat ->
            upsertChatWithParticipantsAndCrossRefs(chat)
        }
}
