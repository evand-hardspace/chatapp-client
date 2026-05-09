package com.evandhardspace.chat.database

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn

@SingleIn(AppScope::class)
@Inject
actual class DatabaseFactory(
    private val context: Context,
) {
    actual fun create(): RoomDatabase.Builder<ChatAppDatabase> {
        val dbFile = context.applicationContext.getDatabasePath(ChatAppDatabase.DB_NAME)

        return Room.databaseBuilder(
            context.applicationContext,
            dbFile.absolutePath,
        )
    }
}
