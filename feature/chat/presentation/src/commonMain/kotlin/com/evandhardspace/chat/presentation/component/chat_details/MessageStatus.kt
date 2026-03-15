package com.evandhardspace.chat.presentation.component.chat_details

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chatapp.core.design_system.generated.resources.check_icon
import chatapp.core.design_system.generated.resources.loading_icon
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.failed
import chatapp.feature.chat.presentation.generated.resources.sending
import chatapp.feature.chat.presentation.generated.resources.sent
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.labelExtraSmall
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import chatapp.core.design_system.generated.resources.Res as DesignSystemRes

@Composable
internal fun MessageStatus(
    status: DeliveryStatus,
    modifier: Modifier = Modifier,
) {
    val (text, icon, color) = when(status) {
        DeliveryStatus.Sending -> Triple(
            stringResource(Res.string.sending),
            vectorResource(DesignSystemRes.drawable.loading_icon),
            MaterialTheme.colorScheme.extended.textDisabled,
        )
        DeliveryStatus.Sent -> Triple(
            stringResource(Res.string.sent),
            vectorResource(DesignSystemRes.drawable.check_icon),
            MaterialTheme.colorScheme.extended.textTertiary,
        )
        DeliveryStatus.Failed -> Triple(
            stringResource(Res.string.failed),
            Icons.Default.Close,
            MaterialTheme.colorScheme.error,
        )
    }

    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = icon,
            contentDescription = text,
            tint = color,
            modifier = Modifier.size(14.dp),
        )
        Spacer(modifier = Modifier.width(MaterialTheme.paddings.quarter))
        Text(
            text = text,
            color = color,
            style = MaterialTheme.typography.labelExtraSmall,
        )
    }
}
