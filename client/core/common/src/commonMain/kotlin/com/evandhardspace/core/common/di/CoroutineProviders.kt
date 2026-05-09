package com.evandhardspace.core.common.di

import com.evandhardspace.core.common.coroutines.DispatcherProvider
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metro.Provides
import dev.zacsweers.metro.SingleIn
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.SupervisorJob

@ContributesTo(AppScope::class)
interface CoroutineProviders {
    @ApplicationScope
    @SingleIn(AppScope::class)
    @Provides
    fun providesApplicationScope(): CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.IO)

    @Provides
    fun providesDispatcherProvider(): DispatcherProvider = object : DispatcherProvider {
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
    }
}
