package com.evandhardspace.chatapp.deeplink.fallback

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.composeapp.generated.resources.Res
import chatapp.composeapp.generated.resources.link_is_not_supported
import chatapp.composeapp.generated.resources.return_back
import chatapp.composeapp.generated.resources.something_went_wrong
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.layout.ChatAppAdaptiveFormLayout
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

@Composable
internal fun DeeplinkFallbackScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
    viewModel: DeeplinkFallbackViewModel = koinViewModel(),
    navigateBack: (isAuthorized: Boolean) -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()

    when (val currentState = state) {
        is DeeplinkFallbackState.Loaded -> DeeplinkFallbackContent(
            modifier = modifier,
            navigateBack = { navigateBack(currentState.isAuthorized) },
        )

        DeeplinkFallbackState.Loading -> Unit // TODO(9): Add loader
    }
}

@Composable
private fun DeeplinkFallbackContent(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
) {
    ChatAppAdaptiveFormLayout(
        modifier = modifier,
        headerText = stringResource(Res.string.something_went_wrong),
        errorText = stringResource(Res.string.link_is_not_supported),
    ) {
        ChatAppButton(
            modifier = Modifier
                .fillMaxWidth(),
            text = stringResource(Res.string.return_back),
            onClick = navigateBack,
        )
    }
}

@ThemedPreview
@Composable
private fun DeeplinkFallbackScreenPreview() {
    ChatAppPreview {
        DeeplinkFallbackContent(
            navigateBack = {},
        )
    }
}
