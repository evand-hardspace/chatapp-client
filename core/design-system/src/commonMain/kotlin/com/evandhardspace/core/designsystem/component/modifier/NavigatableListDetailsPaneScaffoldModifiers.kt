package com.evandhardspace.core.designsystem.component.modifier

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.adaptive.layout.PaneScaffoldTransitionScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.Dp

context(scope: PaneScaffoldTransitionScope<*, *>)
fun Modifier.clipOnTransition(cornerRadius: Dp): Modifier = clip(
    RoundedCornerShape(
        cornerRadius * (1f - scope.motionProgress),
    ),
)
