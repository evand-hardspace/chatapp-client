package com.evandhardspace.chat.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import com.evandhardspace.chat.database.dao.ChatDao
import com.evandhardspace.chat.database.dao.ChatMessageDao
import com.evandhardspace.chat.database.dao.ChatParticipantDao
import com.evandhardspace.chat.database.dao.ChatParticipantsCrossRefDao
import com.evandhardspace.chat.database.entity.ChatEntity
import com.evandhardspace.chat.database.entity.ChatMessageEntity
import com.evandhardspace.chat.database.entity.ChatParticipantCrossRef
import com.evandhardspace.chat.database.entity.ChatParticipantEntity
import com.evandhardspace.chat.database.view.LatestMessageView

@Database(
    entities = [
        ChatEntity::class,
        ChatParticipantEntity::class,
        ChatMessageEntity::class,
        ChatParticipantCrossRef::class,
    ],
    views = [
        LatestMessageView::class,
    ],
    version = 1,
)
@ConstructedBy(ChatAppDatabaseConstructor::class)
abstract class ChatAppDatabase: RoomDatabase() {
    abstract val chatDao: ChatDao
    abstract val chatParticipantDao: ChatParticipantDao
    abstract val chatMessageDao: ChatMessageDao
    abstract val chatParticipantsCrossRefDao: ChatParticipantsCrossRefDao

    companion object {
        const val DB_NAME = "chatapp.db"
    }
}
