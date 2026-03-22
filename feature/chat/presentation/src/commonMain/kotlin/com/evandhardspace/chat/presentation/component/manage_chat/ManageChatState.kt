package com.evandhardspace.chat.presentation.component.manage_chat

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.presentation.util.UiText

data class ManageChatState(
    val queryTextState: TextFieldState = TextFieldState(),
    val localUser: ChatParticipantUi? = null,
    val existingChatParticipants: List<ChatParticipantUi> = emptyList(),
    val selectedChatParticipants: List<ChatParticipantUi> = emptyList(),
    val isSearching: Boolean = false,
    val currentSearchResult: CurrentSearchResultState = CurrentSearchResultState(),
    val searchError: UiText? = null,
    val isSubmitting: Boolean = false,
    val submitError: UiText? = null,
)

data class CurrentSearchResultState(
    val participant: ChatParticipantUi? = null,
    val status: Status = Status.New,
) {
    companion object {
        val Empty = CurrentSearchResultState(null, Status.New)
    }

    enum class Status {
        AlreadySelected,
        AlreadyExists,
        New,
    }
}

fun CurrentSearchResultState.Status?.getOrNew(): CurrentSearchResultState.Status = this ?: CurrentSearchResultState.Status.New
