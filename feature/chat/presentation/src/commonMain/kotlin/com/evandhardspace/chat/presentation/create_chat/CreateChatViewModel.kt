package com.evandhardspace.chat.presentation.create_chat

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.error_participant_not_found
import chatapp.feature.chat.presentation.generated.resources.participant_is_already_selected
import com.evandhardspace.chat.domain.repository.ChatParticipantRepository
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.presentation.component.manage_chat.CurrentSearchResultState
import com.evandhardspace.chat.presentation.component.manage_chat.CurrentSearchResultState.Status
import com.evandhardspace.chat.presentation.component.manage_chat.ManageChatAction
import com.evandhardspace.chat.presentation.component.manage_chat.ManageChatState
import com.evandhardspace.chat.presentation.component.manage_chat.getOrNew
import com.evandhardspace.chat.presentation.manage_chat.DEFAULT_SEARCH_DEBOUNCE_SECONDS
import com.evandhardspace.chat.presentation.mapper.toUi
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Duration.Companion.seconds

@KoinViewModel
internal class CreateChatViewModel(
    private val chatParticipantRepository: ChatParticipantRepository,
    private val charRepository: ChatRepository,
) : ViewModel() {

    private val _effects = Channel<CreateChatEffect>()
    val effects = _effects.receiveAsFlow()

    val state: StateFlow<ManageChatState>
        field = MutableStateFlow(ManageChatState())

    private val searchFlow = snapshotFlow { state.value.queryTextState.text.toString() }
        .debounce(DEFAULT_SEARCH_DEBOUNCE_SECONDS.seconds)
        .onEach(::performSearch)

    init {
        searchFlow.launchIn(viewModelScope)
    }

    fun onAction(action: ManageChatAction) {
        when (action) {
            is ManageChatAction.SelectParticipant -> selectParticipant(action.participant)
            is ManageChatAction.Submit -> createChat()
            is ManageChatAction.RemoveSelectedParticipant -> removeSelectedParticipant(action.participant)
            is ManageChatAction.SelectChat -> Unit
        }
    }

    private fun removeSelectedParticipant(participant: ChatParticipantUi) {
        viewModelScope.launch {
            val newSelectedParticipants = state.value.selectedChatParticipants - participant
            val isAlreadySelected = participant in newSelectedParticipants
            state.update { latestState ->
                latestState.copy(
                    selectedChatParticipants = newSelectedParticipants,
                    currentSearchResult = latestState.currentSearchResult.copy(
                        status = Status.AlreadySelected.takeIf { isAlreadySelected }.getOrNew(),
                    ),
                    searchError = latestState.searchError
                        .takeUnless { latestState.currentSearchResult.status != null },
                )
            }
        }
    }

    private fun selectParticipant(newParticipant: ChatParticipantUi) {
        state.update { latestState ->
            latestState.copy(
                selectedChatParticipants = latestState.selectedChatParticipants + newParticipant,
                currentSearchResult = CurrentSearchResultState.Empty,
                searchError = null,
            )
        }
        state.value.queryTextState.clearText()
    }

    private fun createChat() {
        val userIds = state.value.selectedChatParticipants.map { it.id }
        if (userIds.isEmpty()) {
            return
        }

        viewModelScope.launch {
            state.update {
                it.copy(
                    isSubmitting = true,
                )
            }

            charRepository
                .createChat(userIds)
                .onSuccess { chat ->
                    state.update {
                        it.copy(
                            isSubmitting = false,
                        )
                    }
                    _effects.send(CreateChatEffect.OnChatCreated(chat.id))
                }
                .onFailure { error ->
                    state.update { latestState ->
                        latestState.copy(
                            submitError = error.asUiText(),
                            isSubmitting = false,
                        )
                    }
                }
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            state.update { latestState ->
                latestState.copy(
                    currentSearchResult = CurrentSearchResultState.Empty,
                    searchError = null,
                )
            }
            return
        }

        viewModelScope.launch {
            state.update { latestState ->
                latestState.copy(
                    isSearching = true,
                )
            }

            chatParticipantRepository.searchParticipant(
                query = query,
            ).onSuccess { participant ->
                val isAlreadySelected =
                    state.value.selectedChatParticipants.any { it.id == participant.userId }
                val searchError =
                    if (isAlreadySelected) Res.string.participant_is_already_selected.asUiText()
                    else null

                state.update { latestState ->
                    latestState.copy(
                        currentSearchResult = CurrentSearchResultState(
                            participant = participant.toUi(),
                            status = Status.AlreadySelected.takeIf { isAlreadySelected }.getOrNew(),
                        ),
                        isSearching = false,
                        searchError = searchError,
                    )
                }
            }.onFailure { error ->
                val errorMessage = when (error) {
                    DataError.Remote.NotFound -> Res.string.error_participant_not_found.asUiText()
                    else -> error.asUiText()
                }
                state.update {
                    it.copy(
                        searchError = errorMessage,
                        isSearching = false,
                        currentSearchResult = CurrentSearchResultState.Empty,
                    )
                }
            }
        }
    }
}
