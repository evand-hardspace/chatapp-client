package com.evandhardspace.core.data.di

import com.evandhardspace.core.common.coroutines.DispatcherProvider
import com.evandhardspace.core.common.di.ApplicationScope
import com.evandhardspace.core.data.auth.DataStoreSessionRepository
import com.evandhardspace.core.data.auth.KtorAuthRepository
import com.evandhardspace.core.data.logging.KermitLogger
import com.evandhardspace.core.data.networking.HttpClientFactory
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.logging.ChatAppLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob
import kotlinx.serialization.json.Json
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.annotation.Singleton
import org.koin.core.module.Module as KoinModule
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.binds
import org.koin.dsl.module

internal expect val platformCoreDataModule: KoinModule

@Module
class CoroutineModule {
    @ApplicationScope
    @Singleton
    fun providesApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Factory
    fun providesDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
    }
}

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
