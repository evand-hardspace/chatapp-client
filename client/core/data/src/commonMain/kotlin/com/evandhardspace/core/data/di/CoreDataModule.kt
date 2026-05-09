package com.evandhardspace.core.data.di

import com.evandhardspace.core.data.logging.KermitLogger
import com.evandhardspace.core.data.networking.HttpClientFactory
import com.evandhardspace.core.domain.logging.ChatAppLogger
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.HttpClient
import io.ktor.client.engine.HttpClientEngine
import kotlinx.serialization.json.Json

interface CoreDataProviders {

    @SingleIn(AppScope::class)
    @Provides
    fun provideChatAppLogger(): ChatAppLogger = KermitLogger

    @SingleIn(AppScope::class)
    @Provides
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClientFactory(
        factory: HttpClientFactory,
        engine: HttpClientEngine,
    ): HttpClient = factory.create(engine)
}
