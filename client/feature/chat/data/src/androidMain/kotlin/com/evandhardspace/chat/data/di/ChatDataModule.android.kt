package com.evandhardspace.chat.data.di

import com.evandhardspace.chat.database.DatabaseFactory
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import org.koin.core.module.Module

actual val platformChatDataModule: Module = module {
    single { DatabaseFactory(androidContext()) }
}
