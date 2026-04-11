package com.evandhardspace.chatapp

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.evandhardspace.chatapp.deeplink.ExternalUriHandler

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        var shouldShowSplashScreen = true
        installSplashScreen()
            .setKeepOnScreenCondition { shouldShowSplashScreen }
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        intent?.let(::handleDeeplink)
        setContent {
            App(
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

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}