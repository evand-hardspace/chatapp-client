package com.evandhardspace.chat.presentation.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.group_chat
import chatapp.feature.chat.presentation.generated.resources.you
import com.evandhardspace.chat.presentation.model.ChatUi
import com.evandhardspace.core.designsystem.component.avatar.ChatAppStackedAvatars
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.designsystem.theme.titleExtraSmall
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ChatItemHeaderRow(
    chat: ChatUi,
    isGroupChat: Boolean,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
    ) {
        ChatAppStackedAvatars(
            avatars = chat.otherParticipants,
        )
        Column(
            modifier = Modifier
                .weight(1f),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.quarter),
        ) {
            Text(
                text = if(!isGroupChat) chat.otherParticipants.first().username
                 else stringResource(Res.string.group_chat),
                style = MaterialTheme.typography.titleExtraSmall,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier.fillMaxWidth()
            )
            if(isGroupChat) {
                val you = stringResource(Res.string.you)
                val formattedUsernames = remember(chat.otherParticipants) {
                    "$you, " + chat.otherParticipants.joinToString { it.username }
                }
                Text(
                    text = formattedUsernames,
                    color = MaterialTheme.colorScheme.extended.textPlaceholder,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
