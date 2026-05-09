package com.evandhardspace.chatapp

import com.evandhardspace.chatapp.di.AppGraph
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.DependencyGraph
import dev.zacsweers.metro.createGraph

@DependencyGraph(AppScope::class)
internal interface IOSAppGraph: AppGraph

fun createAppGraph(): AppGraph = createGraph<IOSAppGraph>()