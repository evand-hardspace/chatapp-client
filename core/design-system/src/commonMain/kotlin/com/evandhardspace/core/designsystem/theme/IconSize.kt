package com.evandhardspace.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ChatAppIconSize(
    val medium: Dp = 16.dp,
    val large: Dp = 20.dp,
)

val LocalChatAppIconSize = staticCompositionLocalOf { ChatAppIconSize() }
