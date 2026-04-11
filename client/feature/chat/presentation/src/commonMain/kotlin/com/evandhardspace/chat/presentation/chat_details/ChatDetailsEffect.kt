package com.evandhardspace.chat.presentation.chat_details

import com.evandhardspace.core.presentation.util.UiText

sealed interface ChatDetailsEffect {
    data object ChatLeft : ChatDetailsEffect
    data class Error(val error: UiText): ChatDetailsEffect
    data object NewMessage : ChatDetailsEffect
}
