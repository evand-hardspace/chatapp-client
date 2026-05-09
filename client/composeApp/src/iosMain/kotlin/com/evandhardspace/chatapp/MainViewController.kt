package com.evandhardspace.chatapp

import androidx.compose.ui.window.ComposeUIViewController
import com.evandhardspace.core.navigation.deeplink.DeeplinkProcessor
import dev.zacsweers.metrox.viewmodel.MetroViewModelFactory

fun MainViewController(
    metroViewModelFactory: MetroViewModelFactory,
    deeplinkManager: DeeplinkProcessor,
) = ComposeUIViewController {
    App(
        metroViewModelFactory = metroViewModelFactory,
        deeplinkManager = deeplinkManager,
    )
}
