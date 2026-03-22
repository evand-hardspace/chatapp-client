package com.evandhardspace.core.data.di

import com.evandhardspace.core.data.auth.DataStoreSessionRepository
import com.evandhardspace.core.data.auth.KtorAuthRepository
import com.evandhardspace.core.data.logging.KermitLogger
import com.evandhardspace.core.data.networking.HttpClientFactory
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.logging.ChatAppLogger
import io.ktor.client.HttpClient
import kotlinx.serialization.json.Json
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module
import org.koin.core.module.Module as KoinModule

internal expect val platformCoreDataModule: KoinModule

val coreDataModule = module {
    includes(platformCoreDataModule)

    single<ChatAppLogger> { KermitLogger }
    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single<HttpClient> {
        HttpClientFactory(
            appLogger = get(),
            sessionRepository = get(),
            json = get(),
        ).create(get())
    }
    singleOf(::KtorAuthRepository) bind AuthRepository::class
    single {
        DataStoreSessionRepository(
            dataStore = get(),
            json = get(),
        )
    } binds arrayOf(
        SessionRepository::class,
        MutableSessionRepository::class,
    )
}
