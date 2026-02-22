package com.evandhardspace.auth.presentation.email_verifiaction

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.close
import chatapp.feature.auth.presentation.generated.resources.email_verified_failed
import chatapp.feature.auth.presentation.generated.resources.email_verified_failed_description
import chatapp.feature.auth.presentation.generated.resources.email_verified_successfully
import chatapp.feature.auth.presentation.generated.resources.email_verified_successfully_description
import chatapp.feature.auth.presentation.generated.resources.go_back
import chatapp.feature.auth.presentation.generated.resources.login
import chatapp.feature.auth.presentation.generated.resources.verifying_account
import com.evandhardspace.auth.presentation.email_verifiaction.EmailVerificationState.VerificationState
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.brand.ChatAppFailureIcon
import com.evandhardspace.core.designsystem.component.brand.ChatAppSuccessIcon
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveResultLayout
import com.evandhardspace.core.designsystem.component.layout.ChatAppSimpleResultLayout
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun EmailVerificationScreen(
    viewModel: EmailVerificationViewModel = koinViewModel(),
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    EmailVerificationContent(
        state = state,
        navigateBack = navigateBack,
        navigateToLogin = navigateToLogin,
    )
}

@Composable
internal fun EmailVerificationContent(
    state: EmailVerificationState,
    navigateToLogin: () -> Unit,
    navigateBack: () -> Unit,
) {
    ChatAppAdaptiveResultLayout {
        when (state.verification) {
            is VerificationState.Verifying -> {
                VerifyingContent(
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }

            is VerificationState.Verified -> {
                ChatAppSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_successfully),
                    description = stringResource(Res.string.email_verified_successfully_description),
                    icon = { ChatAppSuccessIcon() },
                    primaryButton = {
                        ChatAppButton(
                            text = stringResource(
                                if (state.verification.isAuthenticated) Res.string.go_back else Res.string.login
                            ),
                            onClick = if (state.verification.isAuthenticated) navigateToLogin else navigateBack,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                )
            }

            is VerificationState.Error -> {
                ChatAppSimpleResultLayout(
                    title = stringResource(Res.string.email_verified_failed),
                    description = stringResource(Res.string.email_verified_failed_description),
                    icon = {
                        Spacer(modifier = Modifier.height(MaterialTheme.paddings.double))
                        ChatAppFailureIcon(
                            modifier = Modifier
                                .size(64.dp),
                        )
                        Spacer(modifier = Modifier.height(MaterialTheme.paddings.double))
                    },
                    primaryButton = {
                        ChatAppButton(
                            text = stringResource(Res.string.close),
                            onClick = if(state.verification.isAuthenticated) navigateBack else navigateToLogin,
                            modifier = Modifier.fillMaxWidth(),
                            style = ChatAppButtonStyle.Secondary,
                        )
                    }
                )
            }
        }
    }
}

@Composable
private fun VerifyingContent(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .heightIn(min = 200.dp)
            .padding(MaterialTheme.paddings.default),
        verticalArrangement = Arrangement.spacedBy(
            MaterialTheme.paddings.default,
            Alignment.CenterVertically
        ),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(48.dp),
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = stringResource(Res.string.verifying_account),
            color = MaterialTheme.colorScheme.extended.textSecondary,
            style = MaterialTheme.typography.bodySmall,
        )
    }
}

@ThemedPreview
@Composable
private fun EmailVerificationErrorPreview() {
    ChatAppPreview {
        EmailVerificationContent(
            state = EmailVerificationState(
                verification = VerificationState.Error(true),
            ),
            navigateBack = {},
            navigateToLogin = {},
        )
    }
}

@ThemedPreview
@Composable
private fun EmailVerificationVerifyingPreview() {
    ChatAppPreview {
        EmailVerificationContent(
            state = EmailVerificationState(
                verification = VerificationState.Verifying,
            ),
            navigateToLogin = {},
            navigateBack = {},
        )
    }
}

@ThemedPreview
@Composable
private fun EmailVerificationSuccessAuthenticatedPreview() {
    ChatAppPreview {
        EmailVerificationContent(
            state = EmailVerificationState(
                verification = VerificationState.Verified(true),
            ),
            navigateBack = {},
            navigateToLogin = {},
        )
    }
}

@ThemedPreview
@Composable
private fun EmailVerificationSuccessNotAuthenticatedPreview() {
    ChatAppPreview {
        EmailVerificationContent(
            state = EmailVerificationState(
                verification = VerificationState.Verified(false),
            ),
            navigateBack = {},
            navigateToLogin = {},
        )
    }
}

