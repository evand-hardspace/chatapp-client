package com.evandhardspace.core.designsystem.component.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chatapp.core.design_system.generated.resources.Res
import chatapp.core.design_system.generated.resources.logo_chatapp
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import org.jetbrains.compose.resources.vectorResource

@Composable
fun ChatappSurface(
    modifier: Modifier = Modifier,
    header: @Composable ColumnScope.() -> Unit = {},
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = modifier
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
                        .fillMaxSize(),
                    content = content,
                )
            }
        }
    }
}

@ThemedPreview
@Composable
fun ChatappSurfacePreview() {
    ChatAppPreview {
        ChatappSurface(
            modifier = Modifier
                .fillMaxSize(),
            header = {
                Icon(
                    imageVector = vectorResource(Res.drawable.logo_chatapp),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier
                        .padding(vertical = 32.dp)
                )
            },
            content = {
                Text(
                    text = "Welcome to Chatapp!",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier
                        .padding(vertical = 40.dp)
                        .align(Alignment.CenterHorizontally),
                )
            }
        )
    }
}
