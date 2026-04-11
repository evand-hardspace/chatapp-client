package com.evandhardspace.chat.presentation.mapper

import chatapp.client.feature.chat.presentation.generated.resources.Res
import chatapp.client.feature.chat.presentation.generated.resources.network_error
import chatapp.client.feature.chat.presentation.generated.resources.offline
import chatapp.client.feature.chat.presentation.generated.resources.online
import chatapp.client.feature.chat.presentation.generated.resources.reconnecting
import chatapp.client.feature.chat.presentation.generated.resources.unknown_error
import com.evandhardspace.chat.domain.model.ConnectionState
import com.evandhardspace.core.presentation.util.UiText
import com.evandhardspace.core.presentation.util.asUiText

internal fun ConnectionState.asUiText(): UiText = when (this) {
    ConnectionState.Disconnected -> Res.string.offline
    ConnectionState.Connecting -> Res.string.reconnecting
    ConnectionState.Connected -> Res.string.online
    ConnectionState.ErrorNetwork -> Res.string.network_error
    ConnectionState.ErrorUnknown -> Res.string.unknown_error
}.asUiText()
