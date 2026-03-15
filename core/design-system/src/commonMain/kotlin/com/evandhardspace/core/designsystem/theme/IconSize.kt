package com.evandhardspace.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ChatAppIconSize(
    val default: Dp = 20.dp,
)

val LocalChatAppIconSize = staticCompositionLocalOf { ChatAppIconSize() }
