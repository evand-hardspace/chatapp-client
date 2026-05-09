package com.evandhardspace.chatapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.evandhardspace.chatapp.deeplink.ExternalUriHandler
import com.evandhardspace.chatapp.di.InjectedViewModelFactory
import com.evandhardspace.core.navigation.deeplink.DeeplinkProcessor
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metro.binding
import dev.zacsweers.metrox.android.ActivityKey

@Inject
@ActivityKey
@ContributesIntoMap(AppScope::class, binding<Activity>())
class MainActivity(
    private val deeplinkManager: DeeplinkProcessor,
    private val metroViewModelFactory: InjectedViewModelFactory,
): ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var shouldShowSplashScreen = true
        installSplashScreen()
            .setKeepOnScreenCondition { shouldShowSplashScreen }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        intent?.let(::handleDeeplink)
        setContent {
            App(
                deeplinkManager = deeplinkManager,
                metroViewModelFactory = metroViewModelFactory,
                onAuthenticationChecked = { shouldShowSplashScreen = false },
            )
        }
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        handleDeeplink(intent)
    }

    private fun handleDeeplink(intent: Intent) {
        intent.data?.toString()?.let(ExternalUriHandler::onNewUri)
    }
}
