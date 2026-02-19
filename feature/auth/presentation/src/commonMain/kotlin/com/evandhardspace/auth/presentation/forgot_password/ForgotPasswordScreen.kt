package com.evandhardspace.auth.presentation.forgot_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.email
import chatapp.feature.auth.presentation.generated.resources.email_placeholder
import chatapp.feature.auth.presentation.generated.resources.forgot_password
import chatapp.feature.auth.presentation.generated.resources.forgot_password_email_sent_successfully
import chatapp.feature.auth.presentation.generated.resources.submit
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppBrandLogo
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveFormLayout
import com.evandhardspace.core.designsystem.component.textfield.ChatAppTextField
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ForgotPasswordScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: ForgotPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ForgotPasswordContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun ForgotPasswordContent(
    state: ForgotPasswordState,
    onAction: (ForgotPasswordAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ChatAppAdaptiveFormLayout(
        modifier = modifier,
        headerText = stringResource(Res.string.forgot_password),
        errorText = state.errorText?.asComposableString(),
        logo = { ChatAppBrandLogo() }
    ) {
        ChatAppTextField(
            state = state.emailTextFieldState,
            modifier = Modifier
                .fillMaxWidth(),
            placeholder = stringResource(Res.string.email_placeholder),
            title = stringResource(Res.string.email),
            isError = state.errorText != null,
            supportingText = state.errorText?.asComposableString(),
            keyboardType = KeyboardType.Email,
            singleLine = true
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
        ChatAppButton(
            text = stringResource(Res.string.submit),
            onClick = {
                onAction(ForgotPasswordAction.OnSubmitClick)
            },
            modifier = Modifier
                .fillMaxWidth(),
            enabled = state.canSubmit,
            isLoading = state.isLoading,
        )
        if (state.isEmailSentSuccessfully) {
            Spacer(Modifier.height(MaterialTheme.paddings.half))
            Text(
                text = stringResource(Res.string.forgot_password_email_sent_successfully),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.extended.success,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
        }
    }
}

@ThemedPreview
@Composable
private fun ForgotPasswordContentPreview() {
    ChatAppPreview {
        ForgotPasswordContent(
            state = ForgotPasswordState(
                isEmailSentSuccessfully = true,
            ),
            onAction = {},
        )
    }
}
