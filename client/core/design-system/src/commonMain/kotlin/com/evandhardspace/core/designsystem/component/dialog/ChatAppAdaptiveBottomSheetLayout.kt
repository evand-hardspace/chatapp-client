package com.evandhardspace.core.designsystem.component.dialog

import androidx.compose.material3.SheetState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class AdaptiveDialogSheetController internal constructor(
    private val isMobile: Boolean,
    val sheetState: SheetState,
    internal val onDismiss: () -> Unit,
    val scope: CoroutineScope,
) {
    fun dismiss() {
        scope.launch {
            if (isMobile) sheetState.hide()
            onDismiss()
        }
    }
}

@Composable
fun rememberAdaptiveDialogSheetController(
    onDismiss: () -> Unit,
): AdaptiveDialogSheetController {
    val scope = rememberCoroutineScope()
    val configuration = currentDeviceConfiguration()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    return remember {
        AdaptiveDialogSheetController(
            isMobile = configuration.isMobile,
            sheetState = sheetState,
            onDismiss = onDismiss,
            scope = scope
        )
    }
}

@Composable
fun ChatAppAdaptiveDialogSheetLayout(
    controller: AdaptiveDialogSheetController,
    content: @Composable () -> Unit
) {
    val configuration = currentDeviceConfiguration()
    if (configuration.isMobile) {
        ChatAppBottomSheet(
            onDismiss = controller.onDismiss,
            content = content,
            sheetState = controller.sheetState,
        )
    } else {
        ChatAppDialogContent(
            onDismiss = controller.onDismiss,
            content = content,
        )
    }
}
