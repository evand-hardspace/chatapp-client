package com.evandhardspace.auth.presentation.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.input.KeyboardType
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.create_account
import chatapp.feature.auth.presentation.generated.resources.email
import chatapp.feature.auth.presentation.generated.resources.email_placeholder
import chatapp.feature.auth.presentation.generated.resources.forgot_password
import chatapp.feature.auth.presentation.generated.resources.login
import chatapp.feature.auth.presentation.generated.resources.password
import chatapp.feature.auth.presentation.generated.resources.welcome_back
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppBrandLogo
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveFormLayout
import com.evandhardspace.core.designsystem.component.textfield.ChatAppPasswordTextField
import com.evandhardspace.core.designsystem.component.textfield.ChatAppTextField
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.ObserveAsEffect
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun LoginScreen(
    viewModel: LoginViewModel = koinViewModel(),
    onLoginSuccess: () -> Unit,
    onForgotPasswordClick: () -> Unit,
    onCreateAccountClick: () -> Unit
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    ObserveAsEffect(viewModel.effects) { effect ->
        when(effect) {
            LoginEffect.Success -> onLoginSuccess()
        }
    }

    LoginContent(
        state = state,
        onAction = { action ->
            when (action) {
                LoginAction.OnForgotPassword -> onForgotPasswordClick()
                LoginAction.OnSignUp -> onCreateAccountClick()
                else -> Unit
            }
            viewModel.onAction(action)
        }
    )
}

@Composable
internal fun LoginContent(
    state: LoginState,
    onAction: (LoginAction) -> Unit,
) {
    ChatAppAdaptiveFormLayout(
        headerText = stringResource(Res.string.welcome_back),
        errorText = state.error?.asComposableString(),
        logo = { ChatAppBrandLogo() },
        modifier = Modifier
            .fillMaxSize()
    ) {
        ChatAppTextField(
            modifier = Modifier
                .fillMaxWidth(),
            state = state.emailTextFieldState,
            placeholder = stringResource(Res.string.email_placeholder),
            supportingText = state.emailError?.asComposableString(),
            isError = state.isEmailValid.not(),
            keyboardType = KeyboardType.Email,
            singleLine = true,
            title = stringResource(Res.string.email),
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.default))
        ChatAppPasswordTextField(
            state = state.passwordTextFieldState,
            placeholder = stringResource(Res.string.password),
            isPasswordVisible = state.isPasswordVisible,
            onToggleVisibilityClick = {
                onAction(LoginAction.OnTogglePasswordVisibility)
            },
            title = stringResource(Res.string.password),
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
        Text(
            text = stringResource(Res.string.forgot_password),
            style = MaterialTheme.typography.titleSmall,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier
                .align(Alignment.End)
                .semantics { role = Role.Button }
                .clip(MaterialTheme.shapes.small)
                .clickable { onAction(LoginAction.OnForgotPassword) }
                .padding(MaterialTheme.paddings.half)
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.fiveQuarters))

        ChatAppButton(
            text = stringResource(Res.string.login),
            onClick = { onAction(LoginAction.OnLogin) },
            enabled = state.canLogin,
            isLoading = state.isLoading,
            modifier = Modifier
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
        ChatAppButton(
            text = stringResource(Res.string.create_account),
            onClick = {
                onAction(LoginAction.OnSignUp)
            },
            style = ChatAppButtonStyle.Secondary,
            modifier = Modifier
                .fillMaxWidth()
        )
    }
}

@ThemedPreview
@Composable
private fun LightThemePreview() {
    ChatAppPreview {
        LoginContent(
            state = LoginState(),
            onAction = {},
        )
    }
}

@ThemedPreview
@Composable
private fun DarkThemePreview() {
    ChatAppPreview {
        LoginContent(
            state = LoginState(),
            onAction = {},
        )
    }
}
