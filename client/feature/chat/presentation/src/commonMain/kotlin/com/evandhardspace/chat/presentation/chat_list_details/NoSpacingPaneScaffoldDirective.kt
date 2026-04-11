package com.evandhardspace.chat.presentation.chat_list_details

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.material3.adaptive.layout.PaneScaffoldDirective
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.DeviceConfiguration
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration

@Composable
fun createNoSpacingPaneScaffoldDirective(): PaneScaffoldDirective {
    val configuration = currentDeviceConfiguration()
    val windowAdaptiveInfo = currentWindowAdaptiveInfo()

    val maxHorizontalPartitions = when(configuration) {
        DeviceConfiguration.MobilePortrait,
        DeviceConfiguration.MobileLandscape,
        DeviceConfiguration.TabletPortrait -> 1
        DeviceConfiguration.TabletLandscape,
        DeviceConfiguration.Desktop -> 2
    }

    val verticalPartitionSpacerSize: Dp
    val maxVerticalPartitions: Int

    if(windowAdaptiveInfo.windowPosture.isTabletop) {
        maxVerticalPartitions = 2
        verticalPartitionSpacerSize = MaterialTheme.paddings.fiveQuarters
    } else {
        maxVerticalPartitions = 1
        verticalPartitionSpacerSize = 0.dp
    }

    return PaneScaffoldDirective(
        maxHorizontalPartitions = maxHorizontalPartitions,
        horizontalPartitionSpacerSize = 0.dp,
        maxVerticalPartitions = maxVerticalPartitions,
        verticalPartitionSpacerSize = verticalPartitionSpacerSize,
        defaultPanePreferredWidth = 360.dp,
        excludedBounds = emptyList(),
    )
}
