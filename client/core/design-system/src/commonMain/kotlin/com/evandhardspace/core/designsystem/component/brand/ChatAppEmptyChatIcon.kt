package com.evandhardspace.core.designsystem.component.brand

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.evandhardspace.core.designsystem.theme.extended

@Composable
fun ChatAppEmptyChatIcon(
    modifier: Modifier = Modifier,
) {
    Text(
        modifier = modifier,
        text = IDKEmoticon,
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.extended.textPrimary,
    )
}

private const val IDKEmoticon = "¯\\_(ツ)_/¯"
