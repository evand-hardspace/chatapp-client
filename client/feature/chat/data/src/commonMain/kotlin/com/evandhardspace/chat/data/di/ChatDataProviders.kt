package com.evandhardspace.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.evandhardspace.chat.database.ChatAppDatabase
import com.evandhardspace.chat.database.DatabaseFactory
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn

@ContributesTo(AppScope::class)
interface ChatDataProviders {
    @SingleIn(AppScope::class)
    @Provides
    fun provideChatAppDatabase(factory: DatabaseFactory): ChatAppDatabase =
        factory.create()
            .setDriver(BundledSQLiteDriver())
            .build()
}
