package com.evandhardspace.core.designsystem.component.layout

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppBrandLogo
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.DesktopMaxWidth
import com.evandhardspace.core.presentation.util.DeviceConfiguration
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration

@Composable
fun ChatAppAdaptiveResultLayout(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val configuration = currentDeviceConfiguration()

    Scaffold(
        modifier = modifier,
        contentWindowInsets = WindowInsets(),
    ) { innerPadding ->
        if (configuration == DeviceConfiguration.MobilePortrait) {
            ChatAppSurface(
                modifier = Modifier
                    .statusBarsPadding()
                    .consumeWindowInsets(
                        WindowInsets.statusBars
                    )
                    .padding(innerPadding),
                header = {
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.double))
                    ChatAppBrandLogo()
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.double))
                },
                content = content,
            )
        } else {
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .systemBarsPadding()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(top = MaterialTheme.paddings.double),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.double)
            ) {
                if (configuration != DeviceConfiguration.MobileLandscape) {
                    ChatAppBrandLogo()
                }
                Column(
                    modifier = Modifier
                        .widthIn(
                            max = DesktopMaxWidth,
                        )
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(horizontal = MaterialTheme.paddings.fiveQuarters)
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.fiveQuarters),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    content = content,
                )
            }
        }
    }
}

@ThemedPreview
@Composable
fun ChatAppAdaptiveResultLayoutPreview() {
    ChatAppPreview(paddings = false) {
        ChatAppAdaptiveResultLayout(
            modifier = Modifier
                .fillMaxSize(),
            content = {
                Text(
                    text = "Registration successful!",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        )
    }
}
