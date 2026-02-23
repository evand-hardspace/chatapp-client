package com.evandhardspace.core.designsystem.component.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
fun ChatAppCallout(
    messageContent: String,
    sender: String,
    formattedDateTime: String,
    calloutPosition: CalloutPosition,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.extended.surfaceHigher,
    messageStatus: @Composable (() -> Unit)? = null,
    onLongClick: (() -> Unit)? = null,
) {
    val tailSize = 8.dp
    val messageContentPadding = 12.dp
    val tailCornerRadius = 20.dp

    Column(
        modifier = modifier
            .then(
                if (onLongClick != null) {
                    Modifier.combinedClickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(
                            color = MaterialTheme.colorScheme.extended.surfaceOutline,
                        ),
                        onLongClick = onLongClick,
                        onClick = {},
                    )
                } else Modifier,
            )
            .clip(
                ChatBubbleShape(
                    calloutPosition = calloutPosition,
                    tailSize = tailSize,
                    cornerRadius = tailCornerRadius,
                )
            )
            .background(color)
            .padding(
                start = if (calloutPosition == CalloutPosition.Start) {
                    messageContentPadding + tailSize
                } else messageContentPadding,
                end = if (calloutPosition == CalloutPosition.End) {
                    messageContentPadding + tailSize
                } else messageContentPadding,
                top = messageContentPadding,
                bottom = messageContentPadding,
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.half),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = sender,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                modifier = Modifier.weight(1f),
            )
            Spacer(modifier = Modifier.width(MaterialTheme.paddings.default))
            Text(
                text = formattedDateTime,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
            )
        }
        Text(
            text = messageContent,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            modifier = Modifier
                .fillMaxWidth(),
        )
        messageStatus?.invoke()
    }
}

@ThemedPreview
@Composable
fun ChatAppCalloutStartPreview() {
    ChatAppPreview(paddings = true) {
        ChatAppCallout(
            messageContent = LoremIpsum(20).values.joinToString(),
            sender = "Ivan",
            formattedDateTime = "Friday 18:20pm",
            calloutPosition = CalloutPosition.Start,
            color = MaterialTheme.colorScheme.extended.accentGreen,
        )
    }
}

@ThemedPreview
@Composable
fun ChatAppCalloutEndPreview() {
    ChatAppPreview(paddings = true) {
        ChatAppCallout(
            messageContent = LoremIpsum(20).values.joinToString(),
            sender = "Ivan",
            formattedDateTime = "Friday 18:20pm",
            calloutPosition = CalloutPosition.End,
        )
    }
}
