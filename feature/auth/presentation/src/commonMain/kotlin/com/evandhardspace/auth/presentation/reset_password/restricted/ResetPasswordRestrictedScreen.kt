package com.evandhardspace.auth.presentation.reset_password.restricted

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.go_back
import chatapp.feature.auth.presentation.generated.resources.password_change_restricted_description
import chatapp.feature.auth.presentation.generated.resources.you_are_logged_in
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveFormLayout
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource

@Composable
fun ResetPasswordRestrictedScreen(
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChatAppAdaptiveFormLayout(
        modifier = modifier,
        headerText = stringResource(Res.string.you_are_logged_in),
    ) {
        Text(
            text = stringResource(Res.string.password_change_restricted_description),
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier
                .fillMaxWidth(),
            textAlign = TextAlign.Center,
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
        ChatAppButton(
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(Res.string.go_back),
            onClick = navigateBack,
            style = ChatAppButtonStyle.Secondary,
        )
    }
}

@ThemedPreview
@Composable
private fun ResetPasswordRestrictedContentPreview() {
    ChatAppPreview {
        ResetPasswordRestrictedScreen(
            navigateBack = {},
        )
    }
}
