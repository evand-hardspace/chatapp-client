package com.evandhardspace.core.designsystem.annotations

import androidx.compose.ui.tooling.preview.AndroidUiModes
import androidx.compose.ui.tooling.preview.Preview

@Preview(
    name = "Light",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_NO,
    showBackground = true,
    backgroundColor = 0xFFCEF5E4,
)
@Preview(
    name = "Dark",
    uiMode = AndroidUiModes.UI_MODE_NIGHT_YES,
    showBackground = true,
    backgroundColor = 0xFF106461,
)
annotation class ThemedPreview