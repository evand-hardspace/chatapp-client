package com.evandhardspace.chat.presentation.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
internal fun ManageChatButtonSection(
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    error: String? = null,
) {
    Column(
        modifier = modifier
            .padding(MaterialTheme.paddings.default),
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(
                MaterialTheme.paddings.default,
                Alignment.End,
            ),
        ) {
            secondaryButton()
            primaryButton()
        }
        AnimatedVisibility(
            visible = error != null,
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
            error?.let {
                Text(
                    text = error,
                    textAlign = TextAlign.End,
                    modifier = Modifier.fillMaxWidth(),
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.labelSmall,
                )
            }
        }
    }
}
