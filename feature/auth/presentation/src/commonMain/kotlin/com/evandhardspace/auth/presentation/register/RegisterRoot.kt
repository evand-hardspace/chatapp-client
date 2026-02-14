package com.evandhardspace.auth.presentation.register

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.email
import chatapp.feature.auth.presentation.generated.resources.email_placeholder
import chatapp.feature.auth.presentation.generated.resources.login
import chatapp.feature.auth.presentation.generated.resources.password
import chatapp.feature.auth.presentation.generated.resources.password_hint
import chatapp.feature.auth.presentation.generated.resources.register
import chatapp.feature.auth.presentation.generated.resources.username
import chatapp.feature.auth.presentation.generated.resources.username_hint
import chatapp.feature.auth.presentation.generated.resources.username_placeholder
import chatapp.feature.auth.presentation.generated.resources.welcome_to_chatapp
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppBrandLogo
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveFormLayout
import com.evandhardspace.core.designsystem.component.snackbar.LocalSnackbarHostState
import com.evandhardspace.core.designsystem.component.textfield.ChatAppPasswordTextField
import com.evandhardspace.core.designsystem.component.textfield.ChatAppTextField
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.ObserveAsEffect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterRoot(
    onRegisterSuccess: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: RegisterViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current

    ObserveAsEffect(viewModel.events) { event ->
        when (event) {
            is RegisterEffect.Success -> onRegisterSuccess(event.email)
        }
    }

    RegisterContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun RegisterContent(
    state: RegisterState,
    onAction: (RegisterAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ChatAppAdaptiveFormLayout(
        modifier = modifier,
        headerText = stringResource(Res.string.welcome_to_chatapp),
        errorText = state.registrationError?.asComposableString(),
        logo = { ChatAppBrandLogo() },
    ) {
        ChatAppTextField(
            state = state.usernameTextState,
            placeholder = stringResource(Res.string.username_placeholder),
            title = stringResource(Res.string.username),
            supportingText = state.usernameError?.asComposableString()
                ?: stringResource(Res.string.username_hint),
            isError = state.usernameError != null,
            onFocusChanged = { isFocused ->
                onAction(RegisterAction.OnInputTextFocusGain)
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
        ChatAppTextField(
            state = state.emailTextState,
            placeholder = stringResource(Res.string.email_placeholder),
            title = stringResource(Res.string.email),
            supportingText = state.emailError?.asComposableString(),
            isError = state.emailError != null,
            onFocusChanged = { isFocused ->
                onAction(RegisterAction.OnInputTextFocusGain)
            }
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
        ChatAppPasswordTextField(
            state = state.passwordTextState,
            placeholder = stringResource(Res.string.password),
            title = stringResource(Res.string.password),
            supportingText = state.passwordError?.asComposableString()
                ?: stringResource(Res.string.password_hint),
            isError = state.passwordError != null,
            onFocusChanged = { isFocused ->
                onAction(RegisterAction.OnInputTextFocusGain)
            },
            onToggleVisibilityClick = {
                onAction(RegisterAction.OnTogglePasswordVisibilityClick)
            },
            isPasswordVisible = state.isPasswordVisible
        )

        Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
        ChatAppButton(
            text = stringResource(Res.string.register),
            onClick = {
                onAction(RegisterAction.OnRegisterClick)
            },
            enabled = state.canRegister,
            isLoading = state.isRegistering,
            modifier = Modifier
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
        ChatAppButton(
            text = stringResource(Res.string.login),
            enabled = state.canLogin,
            onClick = { onAction(RegisterAction.OnLoginClick) },
            style = ChatAppButtonStyle.Secondary,
            modifier = Modifier
                .fillMaxWidth(),
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
    }
}

@ThemedPreview
@Composable
private fun Preview() {
    ChatAppPreview(paddings = false) {
        RegisterContent(
            state = RegisterState(),
            onAction = {},
        )
    }
}
