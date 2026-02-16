package com.evandhardspace.auth.presentation.register_success

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.account_successfully_created
import chatapp.feature.auth.presentation.generated.resources.login
import chatapp.feature.auth.presentation.generated.resources.resend_verification_email
import chatapp.feature.auth.presentation.generated.resources.successfully_resent_verification_email
import chatapp.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppSuccessIcon
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveResultLayout
import com.evandhardspace.core.designsystem.component.layout.ChatAppSimpleResultLayout
import com.evandhardspace.core.designsystem.component.snackbar.LocalSnackbarHostState
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.presentation.util.ObserveAsEffect
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun RegisterSuccessScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: RegisterSuccessViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = LocalSnackbarHostState.current

    ObserveAsEffect(viewModel.effects) { effect ->
        when (effect) {
            is RegisterSuccessEffect.ResendVerificationEmailSuccess -> {
                snackbarHostState.show(
                    message = getString(Res.string.successfully_resent_verification_email),
                )
            }
        }
    }

    RegisterSuccessContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
internal fun RegisterSuccessContent(
    state: RegisterSuccessState,
    onAction: (RegisterSuccessAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ChatAppAdaptiveResultLayout(modifier) {
        ChatAppSimpleResultLayout(
            title = stringResource(Res.string.account_successfully_created),
            description = stringResource(
                Res.string.verification_email_sent_to_x,
                state.registeredEmail,
            ),
            icon = { ChatAppSuccessIcon() },
            primaryButton = {
                ChatAppButton(
                    text = stringResource(Res.string.login),
                    onClick = { onAction(RegisterSuccessAction.OnLogin) },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            },
            secondaryButton = {
                ChatAppButton(
                    text = stringResource(Res.string.resend_verification_email),
                    onClick = { onAction(RegisterSuccessAction.OnResendVerificationEmail) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !state.isResendingVerificationEmail,
                    isLoading = state.isResendingVerificationEmail,
                    style = ChatAppButtonStyle.Secondary,
                )
            },
            secondaryError = state.resendVerificationError?.asComposableString(),
        )
    }
}

@ThemedPreview
@Composable
private fun Preview() {
    ChatAppPreview(paddings = false) {
        RegisterSuccessContent(
            state = RegisterSuccessState(
                registeredEmail = "test@preview.com"
            ),
            onAction = {},
        )
    }
}
