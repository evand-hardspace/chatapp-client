package com.evandhardspace.core.designsystem.component.button

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings

enum class ChatAppButtonStyle {
    Primary,
    DestructivePrimary,
    Secondary,
    DestructiveSecondary,
    Text,
}

@Composable
fun ChatAppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    style: ChatAppButtonStyle = ChatAppButtonStyle.Primary,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    leadingIcon: @Composable (() -> Unit)? = null,
) {
    val colors = when (style) {
        ChatAppButtonStyle.Primary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled,
        )

        ChatAppButtonStyle.DestructivePrimary -> ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            disabledContainerColor = MaterialTheme.colorScheme.extended.disabledFill,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled,
        )

        ChatAppButtonStyle.Secondary -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.extended.textSecondary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled,
        )

        ChatAppButtonStyle.DestructiveSecondary -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.error,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled,
        )

        ChatAppButtonStyle.Text -> ButtonDefaults.buttonColors(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.tertiary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.extended.textDisabled,
        )
    }

    val defaultBorderStroke = BorderStroke(
        width = 1.5.dp,
        color = MaterialTheme.colorScheme.outline, // TODO(3): check if not MaterialTheme.colorScheme.extended.disabledOutline
    )
    val border = when {
        style == ChatAppButtonStyle.Primary && !enabled -> defaultBorderStroke
        style == ChatAppButtonStyle.Secondary -> defaultBorderStroke
        style == ChatAppButtonStyle.DestructivePrimary && !enabled -> defaultBorderStroke
        style == ChatAppButtonStyle.DestructiveSecondary -> {
            val borderColor = if (enabled) {
                MaterialTheme.colorScheme.error // TODO(3): MaterialTheme.colorScheme.extended.destructiveSecondaryOutline
            } else {
                MaterialTheme.colorScheme.outline // TODO(3): MaterialTheme.colorScheme.extended.disabledOutline
            }
            BorderStroke(
                width = 1.5.dp,
                color = borderColor,
            )
        }

        else -> null
    }

    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = MaterialTheme.shapes.small,
        colors = colors,
        border = border,
    ) {
        Box(
            contentAlignment = Alignment.Center,
        ) {
            CircularProgressIndicator(
                modifier = Modifier
                    .size(15.dp)
                    .alpha(
                        alpha = if (isLoading) 1f else 0f,
                    ),
                strokeWidth = 1.5.dp,
                color = Color.Black,
            )
            Row(
                horizontalArrangement = Arrangement.spacedBy(
                    MaterialTheme.paddings.half,
                    Alignment.CenterHorizontally,
                ),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.alpha(
                    if (isLoading) 0f else 1f,
                )
            ) {
                leadingIcon?.invoke()
                Text(
                    text = text,
                    style = MaterialTheme.typography.titleSmall,
                )
            }
        }
    }
}

@ThemedPreview
@Composable
fun ChatAppPrimaryButtonPreview() {
    ChatAppPreview {
        ChatAppButton(
            text = "Primary Button",
            onClick = {},
            style = ChatAppButtonStyle.Primary,
        )
    }
}

@ThemedPreview
@Composable
fun ChatAppSecondaryButtonPreview() {
    ChatAppPreview {
        ChatAppButton(
            text = "Secondary Button",
            onClick = {},
            style = ChatAppButtonStyle.Secondary,
        )
    }
}

@ThemedPreview
@Composable
fun ChatAppDestructivePrimaryButtonPreview() {
    ChatAppPreview {
        ChatAppButton(
            text = "Destructive Primary",
            onClick = {},
            style = ChatAppButtonStyle.DestructivePrimary,
        )
    }
}

@ThemedPreview
@Composable
fun ChatAppDestructiveSecondaryButtonPreview() {
    ChatAppPreview {
        ChatAppButton(
            text = "Destructive Secondary",
            onClick = {},
            style = ChatAppButtonStyle.DestructiveSecondary,
        )
    }
}

@ThemedPreview
@Composable
fun ChatAppTextButtonPreview() {
    ChatAppPreview {
        ChatAppButton(
            text = "Text Button",
            onClick = {},
            style = ChatAppButtonStyle.Text,
        )
    }
}
