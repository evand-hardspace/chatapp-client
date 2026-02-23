package com.evandhardspace.core.designsystem.component.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import chatapp.core.design_system.generated.resources.Res
import chatapp.core.design_system.generated.resources.dismiss_dialog
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource

private const val DEFAULT_MAX_WIDTH_DP = 480

@Composable
fun ChatAppDialog(
    title: String,
    description: String,
    confirmButtonText: String,
    cancelButtonText: String,
    onConfirmClick: () -> Unit,
    onCancelClick: () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
        )
    ) {
        Box(
            modifier = Modifier
                .padding(
                    horizontal = MaterialTheme.paddings.threeQuarters,
                    vertical = MaterialTheme.paddings.default,
                )
                .widthIn(max = DEFAULT_MAX_WIDTH_DP.dp)
                .background(
                    color = MaterialTheme.colorScheme.surface,
                    shape = MaterialTheme.shapes.large,
                )
        ) {
            Column(
                modifier = Modifier
                    .padding(MaterialTheme.paddings.fiveQuarters),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.default),
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.extended.textPrimary,
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.extended.textSecondary,
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = MaterialTheme.paddings.default),
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters, Alignment.End),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    ChatAppButton(
                        text = cancelButtonText,
                        onClick = onCancelClick,
                        style = ChatAppButtonStyle.Secondary,
                    )
                    ChatAppButton(
                        text = confirmButtonText,
                        onClick = onConfirmClick,
                        style = ChatAppButtonStyle.DestructivePrimary,
                    )
                }
            }
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.TopEnd),
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(Res.string.dismiss_dialog),
                    tint = MaterialTheme.colorScheme.extended.textSecondary,
                )
            }
        }
    }
}

@ThemedPreview
@Composable
private fun ChatAppDialogPreview() {
    ChatAppPreview(paddings = true) {
        ChatAppDialog(
            title = "Delete profile picture?",
            description = "This will permanently delete your profile picture. This cannot be undone.",
            confirmButtonText = "Delete",
            cancelButtonText = "Cancel",
            onConfirmClick = {},
            onCancelClick = {},
            onDismiss = {},
        )
    }
}
