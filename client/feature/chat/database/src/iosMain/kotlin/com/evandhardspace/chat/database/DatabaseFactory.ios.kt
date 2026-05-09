package com.evandhardspace.chat.database

import androidx.room.RoomDatabase
import androidx.room.Room
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.SingleIn
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@SingleIn(AppScope::class)
@Inject
actual class DatabaseFactory {
    actual fun create(): RoomDatabase.Builder<ChatAppDatabase> =
        Room.databaseBuilder(
            name = "${documentDirectory()}/${ChatAppDatabase.DB_NAME}",
        )

    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )

        return requireNotNull(documentDirectory?.path)
    }
}
