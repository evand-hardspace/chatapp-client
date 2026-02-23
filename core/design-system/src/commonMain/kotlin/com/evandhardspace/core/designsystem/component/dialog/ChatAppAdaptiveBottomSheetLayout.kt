package com.evandhardspace.core.designsystem.component.dialog

import androidx.compose.runtime.Composable
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration

@Composable
fun ChatAppAdaptiveDialogSheetLayout(
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    val configuration = currentDeviceConfiguration()
    if(configuration.isMobile) {
        ChatAppBottomSheet(
            onDismiss = onDismiss,
            content = content,
        )
    } else {
        ChatAppDialogContent(
            onDismiss = onDismiss,
            content = content,
        )
    }
}
