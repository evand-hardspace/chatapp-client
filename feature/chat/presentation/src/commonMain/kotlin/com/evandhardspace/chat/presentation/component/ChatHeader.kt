package com.evandhardspace.chat.presentation.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.component.brand.ChatAppHorizontalDivider
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
internal fun ChatHeader(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 80.dp)
                .padding(
                    all = MaterialTheme.paddings.default,
                ),
            contentAlignment = Alignment.Center,
        ) { content() }
        ChatAppHorizontalDivider()
    }
}