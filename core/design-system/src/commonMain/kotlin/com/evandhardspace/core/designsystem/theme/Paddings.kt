package com.evandhardspace.core.designsystem.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class ChatAppPaddings(
    val quarter: Dp = 4.dp,
    val half: Dp = 8.dp,
    val threeQuarters: Dp = 12.dp,
    val default: Dp = 16.dp,
    val fiveQuarters: Dp = 16.dp,
    val double: Dp = 32.dp,
)

val LocalChatAppPaddings = staticCompositionLocalOf { ChatAppPaddings() }