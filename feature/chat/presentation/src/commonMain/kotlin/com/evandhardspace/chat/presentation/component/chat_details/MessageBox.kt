package com.evandhardspace.chat.presentation.component.chat_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import chatapp.core.design_system.generated.resources.cloud_off_icon
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.send
import chatapp.feature.chat.presentation.generated.resources.send_a_message
import com.evandhardspace.chat.domain.model.ConnectionState
import com.evandhardspace.chat.presentation.mapper.asUiText
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.textfield.ChatAppMultiLineTextField
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.iconSize
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import chatapp.core.design_system.generated.resources.Res as DesignSystemRes

@Composable
internal fun MessageBox(
    messageTextFieldState: TextFieldState,
    isTextInputEnabled: Boolean,
    connectionState: ConnectionState,
    onSendClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChatAppMultiLineTextField(
        state = messageTextFieldState,
        modifier = modifier
            .padding(MaterialTheme.paddings.quarter),
        placeholder = stringResource(Res.string.send_a_message),
        enabled = isTextInputEnabled,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Send,
        ),
        onKeyboardAction = { onSendClick() },
        bottomContent = {
            Spacer(modifier = Modifier.weight(1f))
            if (connectionState.isConnected.not()) {
                Row(
                    modifier = Modifier.semantics(mergeDescendants = true) { },
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.quarter),
                ) {
                    Icon(
                        modifier = Modifier
                            .semantics { hideFromAccessibility() }
                            .size(MaterialTheme.iconSize.medium),
                        imageVector = vectorResource(DesignSystemRes.drawable.cloud_off_icon),
                        tint = MaterialTheme.colorScheme.extended.textDisabled,
                        contentDescription = null,
                    )
                    Text(
                        text = connectionState.asUiText().asComposableString(),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.extended.textDisabled,
                    )
                }
            }
            ChatAppButton(
                text = stringResource(Res.string.send),
                onClick = onSendClick,
                enabled = connectionState.isConnected && isTextInputEnabled,
            )
        }
    )
}

@ThemedPreview
@Composable
private fun MessageBoxPreview() {
    ChatAppPreview {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            contentAlignment = Alignment.BottomCenter,
        ) {
            MessageBox(
                messageTextFieldState = rememberTextFieldState(),
                isTextInputEnabled = true,
                connectionState = ConnectionState.Disconnected,
                onSendClick = {},
                modifier = Modifier
                    .fillMaxWidth(),
            )
        }
    }
}
