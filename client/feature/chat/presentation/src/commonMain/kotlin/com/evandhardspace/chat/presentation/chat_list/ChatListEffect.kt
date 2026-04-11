package com.evandhardspace.chat.presentation.chat_list

import com.evandhardspace.core.presentation.util.UiText

sealed interface ChatListEffect {
    data class LoggedOutFailed(val error: UiText): ChatListEffect
}
