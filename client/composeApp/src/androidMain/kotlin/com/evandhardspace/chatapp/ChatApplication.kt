package com.evandhardspace.chatapp

import android.app.Application
import dev.zacsweers.metro.createGraphFactory
import dev.zacsweers.metrox.android.MetroAppComponentProviders
import dev.zacsweers.metrox.android.MetroApplication

class ChatApplication : Application(), MetroApplication {
    internal val appGraph by lazy {
        createGraphFactory<AndroidAppGraph.Factory>().create(this)
    }
    override val appComponentProviders: MetroAppComponentProviders
        get() = appGraph
}
