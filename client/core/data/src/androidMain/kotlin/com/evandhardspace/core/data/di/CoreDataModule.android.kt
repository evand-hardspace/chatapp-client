package com.evandhardspace.core.data.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.evandhardspace.client.core.data.BuildKonfig
import com.evandhardspace.core.data.datastore.createAuthInfoDataStore
import com.evandhardspace.core.data.networking.LoopbackHostRewriteInterceptor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.okhttp.OkHttp

@ContributesTo(AppScope::class)
interface AndroidCoreDataProviders: CoreDataProviders {

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClientEngine(): HttpClientEngine = OkHttp.create {
        if (BuildKonfig.FLAVOR == "local") {
            config {
                // Host rewrite is only allowed in application interceptors, not network interceptors.
                addInterceptor(LoopbackHostRewriteInterceptor())
            }
        }
    }

    @SingleIn(AppScope::class)
    @Provides
    fun provideDataStorePreferences(context: Context): DataStore<Preferences> =
        createAuthInfoDataStore(context)
}
