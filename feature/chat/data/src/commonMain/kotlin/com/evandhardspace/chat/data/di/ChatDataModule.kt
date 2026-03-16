package com.evandhardspace.chat.data.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.evandhardspace.chat.database.DatabaseFactory
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module
import org.koin.dsl.module
import org.koin.core.module.Module as KoinModule

@Module
@ComponentScan("com.evandhardspace.chat.data")
class ChatDataModule

internal expect val platformChatDataModule: KoinModule

val chatDataModule: KoinModule = module {
    includes(platformChatDataModule)

    single {
        get<DatabaseFactory>()
            .create()
            .setDriver(BundledSQLiteDriver())
            .build()
    }
}
