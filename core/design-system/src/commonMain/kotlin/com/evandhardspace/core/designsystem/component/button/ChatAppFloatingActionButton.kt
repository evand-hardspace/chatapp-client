package com.evandhardspace.core.designsystem.component.button

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview

@Composable
fun ChatAppFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        content = content,
    )
}

@ThemedPreview
@Composable
fun ChatAppFloatingActionButtonPreview() {
    ChatAppPreview {
        ChatAppFloatingActionButton(
            onClick = {},
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = null,
            )
        }
    }
}
