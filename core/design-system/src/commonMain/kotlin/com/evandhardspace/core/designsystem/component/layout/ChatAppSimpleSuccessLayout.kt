package com.evandhardspace.core.designsystem.component.layout

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppSuccessIcon
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
fun ChatAppSimpleResultLayout(
    title: String,
    description: String,
    icon: @Composable () -> Unit,
    primaryButton: @Composable () -> Unit,
    secondaryButton: @Composable (() -> Unit)? = null,
    secondaryError: String? = null,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .padding(horizontal = MaterialTheme.paddings.default),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        icon()
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .offset(y = -(25).dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.extended.textPrimary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
            Text(
                text = description,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.paddings.fiveQuarters))

            primaryButton()

            if(secondaryButton != null) {
                Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
                secondaryButton()
                if(secondaryError != null) {
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.quarter))
                    Text(
                        text = secondaryError,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.error,
                    )
                }
            }

            Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
        }
    }
}

@ThemedPreview
@Composable
fun ChatAppSimpleResultLayoutPreview() {
    ChatAppPreview(paddings = false) {
        ChatAppSimpleResultLayout(
            title = "Hello world!",
            description = "Test description",
            icon = {
                ChatAppSuccessIcon()
            },
            primaryButton = {
                ChatAppButton(
                    text = "Log In",
                    onClick = {},
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            },
            secondaryButton = {
                ChatAppButton(
                    text = "Resend verification email",
                    onClick = {},
                    style = ChatAppButtonStyle.Secondary,
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            },
        )
    }
}
