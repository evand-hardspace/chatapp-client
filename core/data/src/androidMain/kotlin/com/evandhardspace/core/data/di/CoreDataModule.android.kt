package com.evandhardspace.core.data.di

import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.dsl.module

internal actual val platformCoreDataModule = module {
    single<HttpClientEngine> { OkHttp.create() }
}