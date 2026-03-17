package com.evandhardspace.chat.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.evandhardspace.chat.database.entity.ChatEntity
import com.evandhardspace.chat.database.entity.ChatInfoEntity
import com.evandhardspace.chat.database.entity.ChatMessageEntity
import com.evandhardspace.chat.database.entity.ChatParticipantCrossRef
import com.evandhardspace.chat.database.entity.ChatParticipantEntity
import com.evandhardspace.chat.database.entity.ChatWithParticipants
import kotlinx.coroutines.flow.Flow

@Dao
interface ChatDao {

    @Upsert
    suspend fun upsertChat(chat: ChatEntity)

    @Upsert
    suspend fun upsertChats(chats: List<ChatEntity>)

    @Query("DELETE FROM chatentity WHERE chatId = :chatId")
    suspend fun deleteChatById(chatId: String)

    @Transaction
    @Query("SELECT * FROM chatentity ORDER BY latestActivityAt DESC")
    fun getChatsWithParticipants(): Flow<List<ChatWithParticipants>>

    @Transaction
    @Query(
        """
        SELECT DISTINCT c.*
        FROM chatentity c
        JOIN chatparticipantcrossref cpcr ON c.chatId = cpcr.chatId
        WHERE cpcr.isActive = 1
        ORDER BY latestActivityAt DESC
    """
    )
    fun getChatsWithActiveParticipants(): Flow<List<ChatWithParticipants>>

    @Transaction
    @Query("SELECT * FROM chatentity WHERE chatId = :id")
    suspend fun getChatById(id: String): ChatWithParticipants?

    @Query("DELETE FROM chatentity")
    suspend fun deleteAllChats()

    @Query("SELECT chatId FROM chatentity")
    suspend fun getAllChatIds(): List<String>

    @Transaction
    suspend fun deleteChatsByIds(chatIds: List<String>) {
        chatIds.forEach { chatId ->
            deleteChatById(chatId)
        }
    }

    @Query("SELECT COUNT(*) FROM chatentity")
    fun getChatCount(): Flow<Int>

    @Query(
        """
        SELECT p.*
        FROM chatparticipantentity p
        JOIN chatparticipantcrossref cpcr ON p.userId = cpcr.userId
        WHERE cpcr.chatId = :chatId AND cpcr.isActive = true
        ORDER BY p.username
    """
    )
    fun getActiveParticipantsByChatId(chatId: String): Flow<List<ChatParticipantEntity>>

    @Query("""
        SELECT c.* 
        FROM chatentity c
        JOIN chatparticipantcrossref cpcr ON c.chatId = cpcr.chatId
        WHERE c.chatId = :chatId AND cpcr.isActive = true
    """)
    fun getChatInfoById(chatId: String): Flow<ChatInfoEntity?>

    @Transaction
    suspend fun upsertChatWithParticipantsAndCrossRefs(
        chat: ChatEntity,
        participants: List<ChatParticipantEntity>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantsCrossRefDao
    ) {
        upsertChat(chat)
        participantDao.upsertParticipants(participants)

        val crossRefs = participants.map {
            ChatParticipantCrossRef(
                chatId = chat.chatId,
                userId = it.userId,
                isActive = true,
            )
        }
        crossRefDao.upsertCrossRefs(crossRefs)
        crossRefDao.syncChatParticipants(chat.chatId, participants)
    }

    @Transaction
    suspend fun upsertChatsWithParticipantsAndCrossRefs(
        chats: List<ChatWithParticipants>,
        participantDao: ChatParticipantDao,
        crossRefDao: ChatParticipantsCrossRefDao,
        chatMessageDao: ChatMessageDao,
    ) {
        upsertChats(
            chats.map(ChatWithParticipants::chat),
        )

        val remoteChatIds = chats.map { it.chat.chatId }
        val localChatIds = getAllChatIds()
        val staleChatIds = localChatIds - remoteChatIds.toSet()

        chats.mapNotNull { chat ->
            chat.latestMessage?.run {
                ChatMessageEntity(
                    messageId = messageId,
                    chatId = chat.chat.chatId,
                    senderId = senderId,
                    content = content,
                    timestamp = timestamp,
                    deliveryStatus = deliveryStatus,
                    deliveryStatusTimestamp = timestamp,
                )
            }
        }.let { chatMessageEntities ->
            chatMessageDao.upsertMessages(chatMessageEntities)
        }

        val allParticipants = chats.flatMap(ChatWithParticipants::participants)
        participantDao.upsertParticipants(allParticipants)

        val allCrossRefs = chats.flatMap { chatWithParticipants ->
            chatWithParticipants.participants.map { participant ->
                ChatParticipantCrossRef(
                    chatId = chatWithParticipants.chat.chatId,
                    userId = participant.userId,
                    isActive = true,
                )
            }
        }
        crossRefDao.upsertCrossRefs(allCrossRefs)

        chats.forEach { chat ->
            crossRefDao.syncChatParticipants(
                chatId = chat.chat.chatId,
                participants = chat.participants,
            )
        }

        deleteChatsByIds(staleChatIds)
    }
}
