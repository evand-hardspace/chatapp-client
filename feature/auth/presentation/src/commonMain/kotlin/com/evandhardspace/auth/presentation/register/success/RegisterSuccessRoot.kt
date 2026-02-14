package com.evandhardspace.auth.presentation.register.success

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.account_successfully_created
import chatapp.feature.auth.presentation.generated.resources.login
import chatapp.feature.auth.presentation.generated.resources.resend_verification_email
import chatapp.feature.auth.presentation.generated.resources.verification_email_sent_to_x
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppSuccessIcon
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveResultLayout
import com.evandhardspace.core.designsystem.component.layout.ChatAppSimpleSuccessLayout
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun RegisterSuccessRoot(
    modifier: Modifier = Modifier,
    viewModel: RegisterSuccessViewModel = koinViewModel(),
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    RegisterSuccessContent(
        modifier = modifier,
        state = state,
        onAction = viewModel::onAction,
    )
}

@Composable
fun RegisterSuccessContent(
    state: RegisterSuccessState,
    onAction: (RegisterSuccessAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    ChatAppAdaptiveResultLayout(modifier) {
        ChatAppSimpleSuccessLayout(
            title = stringResource(Res.string.account_successfully_created),
            description = stringResource(
                Res.string.verification_email_sent_to_x,
                state.registeredEmail,
            ),
            icon = { ChatAppSuccessIcon() },
            primaryButton = {
                ChatAppButton(
                    text = stringResource(Res.string.login),
                    onClick = { onAction(RegisterSuccessAction.OnLoginClick) },
                    modifier = Modifier
                        .fillMaxWidth(),
                )
            },
            secondaryButton = {
                ChatAppButton(
                    text = stringResource(Res.string.resend_verification_email),
                    onClick = { onAction(RegisterSuccessAction.OnResendVerificationEmailClick) },
                    modifier = Modifier
                        .fillMaxWidth(),
                    enabled = !state.isResendingVerificationEmail,
                    isLoading = state.isResendingVerificationEmail,
                    style = ChatAppButtonStyle.Secondary,
                )
            }
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
