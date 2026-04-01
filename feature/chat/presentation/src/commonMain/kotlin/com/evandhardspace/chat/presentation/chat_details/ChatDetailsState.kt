package com.evandhardspace.chat.presentation.chat_details

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.chat.domain.model.ConnectionState
import com.evandhardspace.chat.presentation.model.ChatUi
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.presentation.util.UiText

internal data class ChatDetailsState(
    val chatUi: ChatUi? = null,
    val isLoading: Boolean = false,
    val messages: List<MessageUi> = emptyList(),
    val error: UiText? = null,
    val messageTextFieldState: TextFieldState = TextFieldState(),
    val canSendMessage: Boolean = false,
    val isPaginationLoading: Boolean = false,
    val paginationError: UiText? = null,
    val endReached: Boolean = false,
    val bannerState: BannerState = BannerState(),
    val isChatOptionsOpen: Boolean = false,
    val isNearBottom: Boolean = false,
    val connectionState: ConnectionState = ConnectionState.Disconnected,
    val messageWithOpenMenu: MessageUi.LocalUserMessage? = null,
)

internal data class BannerState(
    val formattedDate: UiText? = null,
    val isVisible: Boolean = false,
)
