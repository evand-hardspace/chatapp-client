package com.evandhardspace.chat.presentation.component.manage_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.presentation.util.UiText

data class ManageChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val existingChatParticipants: List<ChatParticipantUi> = emptyList(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val isSearching: Boolean = false,
    val currentSearchResult: CurrentSearchResultState = CurrentSearchResultState(),
    val searchError: UiText? = null,
    val isCreatingChat: Boolean = false,
    val createChatError: UiText? = null,
)

data class CurrentSearchResultState(
    val participant: ChatParticipantUi? = null,
    val isAlreadySelected: Boolean = false,
) {
    companion object {
        val Empty = CurrentSearchResultState(null, false)
    }
}
