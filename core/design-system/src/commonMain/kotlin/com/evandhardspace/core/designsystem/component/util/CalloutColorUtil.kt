package com.evandhardspace.core.designsystem.component.util

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.evandhardspace.core.designsystem.theme.extended

@Composable
fun chatCalloutColor(seed: String): Color = with(MaterialTheme.colorScheme.extended) {
    listOf(
        cakeViolet,
        cakeGreen,
        cakeBlue,
        cakePink,
        cakeOrange,
        cakeYellow,
        cakeTeal,
        cakePurple,
        cakeRed,
        cakeMint,
    )
}.let { colorPool ->
    val index = seed.hashCode().toUInt() % colorPool.size.toUInt()
    colorPool[index.toInt()]
}
