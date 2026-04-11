package com.evandhardspace.core.designsystem.component.layout

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppBrandLogo
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.DesktopMaxWidth
import com.evandhardspace.core.presentation.util.DeviceConfiguration
import com.evandhardspace.core.presentation.util.compose.clearFocusOnTap
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration

@Composable
fun ChatAppAdaptiveFormLayout(
    headerText: String,
    errorText: String? = null,
    logo: @Composable () -> Unit = { ChatAppBrandLogo() },
    modifier: Modifier = Modifier,
    formContent: @Composable ColumnScope.() -> Unit,
) {
    val configuration = currentDeviceConfiguration()
    val headerColor = MaterialTheme.colorScheme.extended.textPrimary

    when (configuration) {
        DeviceConfiguration.MobilePortrait -> {
            ChatAppSurface(
                modifier = modifier
                    .clearFocusOnTap(),
                consumeContentSurfaceInsets = true,
                header = {
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.double))
                    logo()
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.double))
                },
            ) {
                Spacer(modifier = Modifier.height(MaterialTheme.paddings.fiveQuarters))
                AuthHeaderSection(
                    headerText = headerText,
                    headerColor = headerColor,
                    errorText = errorText,
                )
                Spacer(modifier = Modifier.height(MaterialTheme.paddings.fiveQuarters))
                formContent()
            }
        }

        DeviceConfiguration.MobileLandscape -> {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.default),
                modifier = modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
                    .padding(start = MaterialTheme.paddings.default),
            ) {
                Column(
                    modifier = Modifier
                        .systemBarsPadding()
                        .weight(1f),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.fiveQuarters),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
                    logo()
                    AuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText,
                    )
                }
                ChatAppSurface(
                    modifier = Modifier
                        .weight(1f),
                    consumeContentSurfaceInsets = false,
                ) {
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
                    Column(
                        modifier = Modifier.windowInsetsPadding(
                            WindowInsets.systemBars.only(
                                WindowInsetsSides.End,
                            )
                        ),
                        content = formContent,
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
                }
            }
        }

        DeviceConfiguration.TabletPortrait,
        DeviceConfiguration.TabletLandscape,
        DeviceConfiguration.Desktop -> {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .statusBarsPadding() // TODO check
                    .verticalScroll(rememberScrollState())
                    .background(MaterialTheme.colorScheme.background)
                    .padding(vertical = MaterialTheme.paddings.double),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.double)
            ) {
                logo()
                Column(
                    modifier = Modifier
                        .widthIn(max = DesktopMaxWidth)
                        .fillMaxWidth()
                        .clip(MaterialTheme.shapes.extraLarge)
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(
                            horizontal = MaterialTheme.paddings.fiveQuarters,
                            vertical = MaterialTheme.paddings.double,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    AuthHeaderSection(
                        headerText = headerText,
                        headerColor = headerColor,
                        errorText = errorText,
                    )
                    formContent()
                }
            }
        }
    }
}

@Composable
fun ColumnScope.AuthHeaderSection(
    headerText: String,
    headerColor: Color,
    errorText: String? = null,
) {
    Text(
        text = headerText,
        style = MaterialTheme.typography.titleLarge,
        color = headerColor,
        textAlign = TextAlign.Center,
        modifier = Modifier.fillMaxWidth(),
    )
    AnimatedVisibility(
        visible = errorText != null,
    ) {
        if (errorText != null) {
            Text(
                text = errorText,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier
                    .padding(top = MaterialTheme.paddings.half)
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@ThemedPreview
@Composable
fun ChatAppAdaptiveFormLayoutDarkPreview() {
    ChatAppPreview {
        ChatAppAdaptiveFormLayout(
            headerText = "Welcome to ChatApp!",
            errorText = "Login failed!",
            formContent = {
                Text(
                    text = "Sample form title",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
                Text(
                    text = "Sample form title 2",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }
        )
    }
}
