package com.evandhardspace.chat.presentation.profile.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.DeviceConfiguration
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration

@Composable
fun ProfileSectionLayout(
    headerText: String,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit,
) {
    val deviceConfiguration = currentDeviceConfiguration()

    when(deviceConfiguration) {
        DeviceConfiguration.MobilePortrait -> {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.half),
                horizontalAlignment = Alignment.Start,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.paddings.default),
            ) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.extended.textTertiary,
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
                ) { content() }
            }
        }
        else -> {
            Row(
                verticalAlignment = Alignment.Top,
                modifier = modifier
                    .fillMaxWidth()
                    .padding(MaterialTheme.paddings.default),
            ) {
                Text(
                    text = headerText,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.extended.textTertiary,
                    modifier = Modifier.weight(1f),
                )
                Column(
                    modifier = Modifier
                        .weight(3f),
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
                ) { content() }
            }
        }
    }
}
