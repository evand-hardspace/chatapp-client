package com.evandhardspace.chatapp.di

import com.evandhardspace.core.navigation.deeplink.DeeplinkProcessor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesTo
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory
import dev.zacsweers.metrox.viewmodel.ViewModelGraph

interface AppGraph: ViewModelGraph {
    val dependencies: AppDependencies
}

@ContributesTo(AppScope::class)
interface AppDependencies {
    val deeplinkProcessor: DeeplinkProcessor
    val metroViewModelFactory: MetroViewModelFactory
}

