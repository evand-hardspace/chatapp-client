package com.evandhardspace.core.data.di

import com.evandhardspace.core.data.auth.DataStoreSessionRepository
import com.evandhardspace.core.data.auth.KtorAuthService
import com.evandhardspace.core.data.logging.KermitLogger
import com.evandhardspace.core.data.networking.HttpClientFactory
import com.evandhardspace.core.domain.auth.AuthService
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.logging.ChatAppLogger
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.module.Module
import org.koin.core.module.dsl.singleOf
import org.koin.core.qualifier.named
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

internal expect val platformCoreDataModule: Module

internal object AppCoroutineScope

val coreDataModule = module {
    includes(platformCoreDataModule)

    // TODO(10): Provide correct scope
    single(named<AppCoroutineScope>()) { CoroutineScope(SupervisorJob() + Dispatchers.IO) }

    single<ChatAppLogger> { KermitLogger }
    single<Json> {
        Json {
            ignoreUnknownKeys = true
        }
    }
    single {
        HttpClientFactory(
            appLogger = get(),
            sessionRepository = get(),
            json = get(),
        ).create(get())
    }
    singleOf(::KtorAuthService) bind AuthService::class
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
