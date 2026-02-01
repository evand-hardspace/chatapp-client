package com.evandhardspace.core.designsystem.theme

import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.dp

data class ChatAppShapes(
    val small: RoundedCornerShape = RoundedCornerShape(4.dp),
    val default: RoundedCornerShape = RoundedCornerShape(8.dp),
    val large: RoundedCornerShape = RoundedCornerShape(16.dp),
    val round: RoundedCornerShape = CircleShape,
)

val LocalChatAppShapes = staticCompositionLocalOf { ChatAppShapes() }