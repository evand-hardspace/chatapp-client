package com.evandhardspace.chat.presentation.profile.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chatapp.client.feature.chat.presentation.generated.resources.cancel
import chatapp.client.feature.chat.presentation.generated.resources.profile_settings
import com.evandhardspace.core.designsystem.component.button.ChatAppIconButton
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.iconSize
import org.jetbrains.compose.resources.stringResource
import chatapp.client.feature.chat.presentation.generated.resources.Res

@Composable
internal fun ProfileHeaderSection(
    username: String,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier
                .weight(1f),
        ) {
            Text(
                text = username,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary,
            )
            Text(
                text = stringResource(Res.string.profile_settings),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
            )
        }
        ChatAppIconButton(
            onClick = onCloseClick,
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(Res.string.cancel),
                modifier = Modifier.size(MaterialTheme.iconSize.large),
                tint = MaterialTheme.colorScheme.extended.textSecondary,
            )
        }
    }
}
