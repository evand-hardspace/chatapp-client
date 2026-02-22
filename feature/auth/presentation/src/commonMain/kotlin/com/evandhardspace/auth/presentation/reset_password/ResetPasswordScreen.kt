package com.evandhardspace.auth.presentation.reset_password

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.password
import chatapp.feature.auth.presentation.generated.resources.password_changed
import chatapp.feature.auth.presentation.generated.resources.password_hint
import chatapp.feature.auth.presentation.generated.resources.reset_password_successfully
import chatapp.feature.auth.presentation.generated.resources.return_to_login
import chatapp.feature.auth.presentation.generated.resources.set_new_password
import chatapp.feature.auth.presentation.generated.resources.submit
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppBrandLogo
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveFormLayout
import com.evandhardspace.core.designsystem.component.textfield.ChatAppPasswordTextField
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun ResetPasswordScreen(
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: ResetPasswordViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ResetPasswordContent(
        modifier = modifier,
        state = state,
        navigateToLogin = navigateToLogin,
        onAction = viewModel::onAction,
    )
}

@Composable
private fun ResetPasswordContent(
    state: ResetPasswordState,
    onAction: (ResetPasswordAction) -> Unit,
    navigateToLogin: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChatAppAdaptiveFormLayout(
        modifier = modifier,
        headerText = stringResource(
            if (state.isPasswordChanged) Res.string.password_changed
            else Res.string.set_new_password,
        ),
        errorText = state.errorText?.asComposableString(),
    ) {
        if (state.isPasswordChanged) {
            Text(
                text = stringResource(Res.string.reset_password_successfully),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier
                    .fillMaxWidth(),
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
            ChatAppButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.return_to_login),
                onClick = navigateToLogin,
            )
        } else {
            ChatAppPasswordTextField(
                state = state.passwordTextState,
                modifier = Modifier
                    .fillMaxWidth(),
                placeholder = stringResource(Res.string.password),
                title = stringResource(Res.string.password),
                supportingText = stringResource(Res.string.password_hint),
                isPasswordVisible = state.isPasswordVisible,
                onToggleVisibilityClick = { onAction(ResetPasswordAction.OnTogglePasswordVisibility) },
            )
            Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
            ChatAppButton(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(Res.string.submit),
                onClick = { onAction(ResetPasswordAction.OnSubmit) },
                enabled = !state.isLoading && state.canSubmit,
                isLoading = state.isLoading
            )
        }
    }
}

@ThemedPreview
@Composable
private fun ResetPasswordContentPreview() {
    ChatAppPreview {
        ResetPasswordContent(
            state = ResetPasswordState(
                isPasswordChanged = true,
            ),
            onAction = {},
            navigateToLogin = {},
        )
    }
}