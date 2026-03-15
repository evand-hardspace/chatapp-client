package com.evandhardspace.chat.presentation.mapper

import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.network_error
import chatapp.feature.chat.presentation.generated.resources.offline
import chatapp.feature.chat.presentation.generated.resources.online
import chatapp.feature.chat.presentation.generated.resources.reconnecting
import chatapp.feature.chat.presentation.generated.resources.unknown_error
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
