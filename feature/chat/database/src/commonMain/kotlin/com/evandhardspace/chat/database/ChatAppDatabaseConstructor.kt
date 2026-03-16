package com.evandhardspace.chat.database

import androidx.room.RoomDatabaseConstructor

expect object ChatAppDatabaseConstructor: RoomDatabaseConstructor<ChatAppDatabase> {
    override fun initialize(): ChatAppDatabase
}
