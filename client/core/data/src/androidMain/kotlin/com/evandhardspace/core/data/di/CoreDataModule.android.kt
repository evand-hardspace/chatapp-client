package com.evandhardspace.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.evandhardspace.client.core.data.BuildKonfig
import com.evandhardspace.core.data.datastore.createAuthInfoDataStore
import com.evandhardspace.core.data.networking.LoopbackHostRewriteInterceptor
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

internal actual val platformCoreDataModule = module {
    single<HttpClientEngine> {
        OkHttp.create {
            if (BuildKonfig.FLAVOR == "local") {
                config {
                    // Host rewrite is only allowed in application interceptors, not network interceptors.
                    addInterceptor(LoopbackHostRewriteInterceptor())
                }
            }
        }
    }
    single<DataStore<Preferences>> {
        createAuthInfoDataStore(androidContext())
    }
}
