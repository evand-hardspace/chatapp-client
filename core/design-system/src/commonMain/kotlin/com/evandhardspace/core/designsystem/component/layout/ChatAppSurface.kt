package com.evandhardspace.core.designsystem.component.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.layout.windowInsetsTopHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppBrandLogo
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
fun ChatAppSurface(
    modifier: Modifier = Modifier,
    consumeContentSurfaceInsets: Boolean,
    header: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit,
) {
    Surface(
        modifier = modifier,
        color = MaterialTheme.colorScheme.background,
    ) {
        Surface(
            color = MaterialTheme.colorScheme.background,
            modifier = Modifier.statusBarsPadding(),
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .fillMaxSize(),
            ) {
                header()
                Surface(
                    color = MaterialTheme.colorScheme.surface,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),
                    shape = MaterialTheme.shapes.extraLarge.copy(
                        bottomStart = CornerSize(0.dp),
                        bottomEnd = CornerSize(0.dp),
                    ),
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(horizontal = MaterialTheme.paddings.default)
                            .padding(top = MaterialTheme.paddings.half)
                            .let { upstreamModifier ->
                                if (consumeContentSurfaceInsets) upstreamModifier
                                    .consumeWindowInsets(WindowInsets.systemBars)
                                else upstreamModifier
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Spacer(Modifier.windowInsetsTopHeight(WindowInsets.systemBars))
                        content()
                        Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.systemBars))
                    }
                }
            }
        }
    }
}

@ThemedPreview
@Composable
fun ChatAppSurfacePreview() {
    ChatAppPreview {
        ChatAppSurface(
            modifier = Modifier
                .fillMaxSize(),
            consumeContentSurfaceInsets = true,
            header = {
                ChatAppBrandLogo(
                    modifier = Modifier
                        .padding(vertical = 32.dp),
                )
            },
            content = {
                Text(
                    text = "Welcome to Chatapp!",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 24.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        )
    }
}
