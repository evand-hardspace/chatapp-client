package com.evandhardspace.core.designsystem.component.dialog

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview

@Composable
fun ChatAppDialogContent(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
    ) {
        Surface(
            modifier = modifier
                .fillMaxWidth()
                .clip(MaterialTheme.shapes.medium),
            color = MaterialTheme.colorScheme.surface,
            content = content,
        )
    }
}

@ThemedPreview
@Composable
private fun ChatAppDialogContentPreview() {
    ChatAppPreview(paddings = true) {
        ChatAppDialogContent(
            onDismiss = {},
        ) {
            Box(modifier = Modifier.padding(8.dp),
                contentAlignment = Alignment.Center,
            ) {
                Text("Dialog Content")
            }
        }
    }
}
