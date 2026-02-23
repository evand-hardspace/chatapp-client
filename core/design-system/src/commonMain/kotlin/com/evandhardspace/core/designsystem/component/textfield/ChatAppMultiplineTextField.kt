package com.evandhardspace.core.designsystem.component.textfield

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.KeyboardActionHandler
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import androidx.compose.ui.unit.dp
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings

@Composable
fun ChatAppMultiLineTextField(
    state: TextFieldState,
    modifier: Modifier = Modifier,
    placeholder: String? = null,
    enabled: Boolean = true,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default,
    onKeyboardAction: KeyboardActionHandler? = null,
    bottomContent: @Composable (RowScope.() -> Unit)? = null,
) {
    Column(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.extended.surfaceLower,
                shape = MaterialTheme.shapes.large,
            )
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.extended.surfaceOutline,
                shape = MaterialTheme.shapes.large,
            )
            .padding(
                vertical = MaterialTheme.paddings.threeQuarters,
                horizontal = MaterialTheme.paddings.default,
            ),
        verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.half),
    ) {
        BasicTextField(
            state = state,
            modifier = Modifier
                .weight(1f),
            enabled = enabled,
            textStyle = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.extended.textPrimary
            ),
            keyboardOptions = keyboardOptions,
            onKeyboardAction = onKeyboardAction,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.extended.textPrimary),
            decorator = { innerBox ->
                if(placeholder != null && state.text.isNotEmpty()) {
                    Text(
                        text = placeholder,
                        color = MaterialTheme.colorScheme.extended.textPlaceholder,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
                innerBox()
            },
        )
        if(bottomContent != null) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                this.bottomContent()
            }
        }
    }
}

@ThemedPreview
@Composable
fun ChatAppMultiLineTextFieldPreview() {
    ChatAppPreview {
        ChatAppMultiLineTextField(
            state = rememberTextFieldState(
                initialText = LoremIpsum(20).values.joinToString(),
            ),
            modifier = Modifier
                .widthIn(max = 300.dp)
                .heightIn(min = 150.dp),
            placeholder = null,
            bottomContent = {
                Spacer(modifier = Modifier.weight(1f))
                ChatAppButton(
                    text = "Send",
                    onClick = {},
                )
            }
        )
    }
}
