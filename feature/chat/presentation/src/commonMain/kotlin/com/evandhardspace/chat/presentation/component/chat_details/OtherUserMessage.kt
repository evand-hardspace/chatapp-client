package com.evandhardspace.chat.presentation.component.chat_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.designsystem.component.avatar.ChatAppAvatarPhoto
import com.evandhardspace.core.designsystem.component.chat.CalloutPosition
import com.evandhardspace.core.designsystem.component.chat.ChatAppCallout
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
internal fun OtherUserMessage(
    message: MessageUi.OtherUserMessage,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.half),
    ) {
        ChatAppAvatarPhoto(
            displayText = message.sender.initials,
            imageUrl = message.sender.imageUrl,
        )
        ChatAppCallout(
            messageContent = message.content,
            sender = message.sender.username,
            calloutPosition = CalloutPosition.Start,
            formattedDateTime = message.formattedSentTime.asComposableString(),
        )
    }
}
