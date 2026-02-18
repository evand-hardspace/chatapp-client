package com.evandhardspace.chatapp.di

import com.evandhardspace.auth.presentation.di.authPresentationModule
import com.evandhardspace.core.data.di.coreDataModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule,
            coreDataModule,
            authPresentationModule,
        )
    }
}