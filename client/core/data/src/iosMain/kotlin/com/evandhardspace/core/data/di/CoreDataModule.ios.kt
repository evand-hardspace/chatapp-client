package com.evandhardspace.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.evandhardspace.core.data.datastore.createAuthInfoDataStore
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import io.ktor.client.engine.HttpClientEngine
import io.ktor.client.engine.darwin.Darwin

@ContributesTo(AppScope::class)
interface IOSCoreDataProviders: CoreDataProviders {

    @SingleIn(AppScope::class)
    @Provides
    fun provideHttpClientEngine(): HttpClientEngine = Darwin.create()

    @SingleIn(AppScope::class)
    @Provides
    fun provideDataStorePreferences(): DataStore<Preferences> = createAuthInfoDataStore()
}
