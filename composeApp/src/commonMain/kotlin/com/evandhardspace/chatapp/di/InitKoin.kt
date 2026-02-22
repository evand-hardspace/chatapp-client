package com.evandhardspace.chatapp.di

import com.evandhardspace.chatapp.ChatappKoinApp
import com.evandhardspace.core.data.di.coreDataModule
import org.koin.dsl.KoinAppDeclaration
import org.koin.plugin.module.dsl.startKoin

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin<ChatappKoinApp> {
        config?.invoke(this)
        modules(
            coreDataModule,
        )
    }
}