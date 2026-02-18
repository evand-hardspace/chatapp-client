package com.evandhardspace.core.data.di

import com.evandhardspace.core.data.auth.DataStoreSessionStorage
import com.evandhardspace.core.data.auth.KtorAuthService
import com.evandhardspace.core.data.logging.KermitLogger
import com.evandhardspace.core.data.networking.HttpClientFactory
import com.evandhardspace.core.domain.auth.AuthService
import com.evandhardspace.core.domain.auth.MutableSessionStorage
import com.evandhardspace.core.domain.auth.SessionStorage
import com.evandhardspace.core.domain.logging.ChatAppLogger
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

internal expect val platformCoreDataModule: Module

val coreDataModule = module {
    includes(platformCoreDataModule)

    single<ChatAppLogger> { KermitLogger }
    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        HttpClientFactory(
            appLogger = get(),
            sessionStorage = get(),
            json = get(),
        ).create(get())
    }
    singleOf(::KtorAuthService) bind AuthService::class
    singleOf(::DataStoreSessionStorage) binds arrayOf(
        SessionStorage::class,
        MutableSessionStorage::class,
    )
}
