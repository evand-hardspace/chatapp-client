package com.evandhardspace.chat.presentation.chat_details.component

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import chatapp.core.design_system.generated.resources.reload_icon
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.delete_for_everyone
import chatapp.feature.chat.presentation.generated.resources.retry
import chatapp.feature.chat.presentation.generated.resources.you
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.designsystem.component.chat.CalloutPosition
import com.evandhardspace.core.designsystem.component.chat.ChatAppCallout
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import chatapp.core.design_system.generated.resources.Res as DesignSystemRes

@Composable
internal fun LocalUserMessage(
    message: MessageUi.LocalUserMessage,
    onMessageLongClick: () -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.half, Alignment.End),
    ) {
        Box(
            modifier = Modifier.weight(1f)
        ) {
            ChatAppCallout(
                messageContent = message.content,
                sender = stringResource(Res.string.you),
                formattedDateTime = message.formattedSentTime.asComposableString(),
                calloutPosition = CalloutPosition.End,
                messageStatus = {
                    MessageStatus(
                        status = message.deliveryStatus,
                    )
                },
                onLongClick = onMessageLongClick,
            )

            DropdownMenu(
                expanded = message.isMenuOpen,
                onDismissRequest = onDismissMessageMenu,
                containerColor = MaterialTheme.colorScheme.surface,
                shape = MaterialTheme.shapes.small,
                border = BorderStroke(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.extended.surfaceOutline,
                )
            ) {
                DropdownMenuItem(
                    text = {
                        Text(
                            text = stringResource(Res.string.delete_for_everyone),
                            color = MaterialTheme.colorScheme.extended.destructiveHover,
                            fontWeight = FontWeight.Medium
                        )
                    },
                    onClick = {
                        onDismissMessageMenu()
                        onDeleteClick()
                    }
                )
            }
        }

        if(message.deliveryStatus == DeliveryStatus.Failed) {
            IconButton(
                onClick = onRetryClick,
            ) {
                Icon(
                    imageVector = vectorResource(DesignSystemRes.drawable.reload_icon),
                    contentDescription = stringResource(Res.string.retry),
                    tint = MaterialTheme.colorScheme.error,
                )
            }
        }
    }
}
