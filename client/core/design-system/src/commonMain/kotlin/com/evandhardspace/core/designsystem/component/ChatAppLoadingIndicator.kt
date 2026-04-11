package com.evandhardspace.core.designsystem.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview

@Composable
fun ChatAppLoadingIndicator(
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 4.dp
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = MaterialTheme.colorScheme.onSurface,
        strokeWidth = strokeWidth,
    )
}

@Composable
fun ChatAppLoadingSpace(
    modifier: Modifier,
) {
    Surface {
        Box(
            modifier = modifier,
            contentAlignment = Alignment.Center,
        ) {
            ChatAppLoadingIndicator(modifier = Modifier.size(48.dp))
        }
    }
}

@ThemedPreview
@Composable
private fun ChatAppLoadingSpacePreview() {
    ChatAppPreview {
        ChatAppLoadingSpace(Modifier.fillMaxSize())
    }
}