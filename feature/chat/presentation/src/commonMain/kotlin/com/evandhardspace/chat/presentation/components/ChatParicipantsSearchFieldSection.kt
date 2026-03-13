package com.evandhardspace.chat.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.add
import chatapp.feature.chat.presentation.generated.resources.email_or_username
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.textfield.ChatAppTextField
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.UiText
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ChatParticipantSearchTextSection(
    queryState: TextFieldState,
    onAddClick: () -> Unit,
    isSearchEnabled: Boolean,
    isLoading: Boolean,
    modifier: Modifier = Modifier,
    error: UiText? = null,
    onFocusChanged: (Boolean) -> Unit,
) {
    Row(
        modifier = modifier
            .padding(MaterialTheme.paddings.default),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.default),
    ) {
        ChatAppTextField(
            state = queryState,
            modifier = Modifier
                .weight(1f),
            placeholder = stringResource(Res.string.email_or_username),
            title = null,
            supportingText = error?.asComposableString(),
            isError = error != null,
            singleLine = true,
            keyboardType = KeyboardType.Email,
            onFocusChanged = onFocusChanged
        )
        ChatAppButton(
            text = stringResource(Res.string.add),
            onClick = onAddClick,
            style = ChatAppButtonStyle.Secondary,
            enabled = isSearchEnabled,
            isLoading = isLoading,
        )
    }
}
