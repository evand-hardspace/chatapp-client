package com.evandhardspace.chat.presentation.component.chat_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import chatapp.core.design_system.generated.resources.reload_icon
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.delete_for_everyone
import chatapp.feature.chat.presentation.generated.resources.retry
import chatapp.feature.chat.presentation.generated.resources.you
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.designsystem.component.chat.CalloutPosition
import com.evandhardspace.core.designsystem.component.chat.ChatAppCallout
import com.evandhardspace.core.designsystem.component.dropdown.ChatAppDropdownMenu
import com.evandhardspace.core.designsystem.component.dropdown.DropdownItem
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import chatapp.core.design_system.generated.resources.Res as DesignSystemRes

@Composable
internal fun LocalUserMessage(
    message: MessageUi.LocalUserMessage,
    showMenu: Boolean,
    onMessageLongClick: () -> Unit,
    onDismissMessageMenu: () -> Unit,
    onDeleteClick: () -> Unit,
    onRetryClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val onDeleteClick = {
        onDismissMessageMenu()
        onDeleteClick()
    }
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.half, Alignment.End),
    ) {
        Box {
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

            ChatAppDropdownMenu(
                expanded = showMenu,
                onDismiss = onDismissMessageMenu,
                items = listOf(
                    DropdownItem(
                        title = stringResource(Res.string.delete_for_everyone),
                        contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                        onClick = onDeleteClick,
                    ),
                )
            )
        }

        if (message.deliveryStatus == DeliveryStatus.Failed) {
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
