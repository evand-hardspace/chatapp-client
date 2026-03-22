package com.evandhardspace.chat.presentation.manage_chat

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.error_participant_not_found
import chatapp.feature.chat.presentation.generated.resources.participant_is_already_exists
import chatapp.feature.chat.presentation.generated.resources.participant_is_already_selected
import com.evandhardspace.chat.domain.model.ChatParticipant
import com.evandhardspace.chat.domain.repository.ChatParticipantRepository
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.presentation.component.manage_chat.CurrentSearchResultState
import com.evandhardspace.chat.presentation.component.manage_chat.CurrentSearchResultState.Status
import com.evandhardspace.chat.presentation.component.manage_chat.ManageChatAction
import com.evandhardspace.chat.presentation.component.manage_chat.ManageChatState
import com.evandhardspace.chat.presentation.component.manage_chat.getOrNew
import com.evandhardspace.chat.presentation.mapper.toUi
import com.evandhardspace.core.common.stateInUi
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Duration.Companion.seconds

internal const val DEFAULT_SEARCH_DEBOUNCE_SECONDS = 1

@KoinViewModel
class ManageChatViewModel(
    private val chatRepository: ChatRepository,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {
    private val _effects = Channel<ManageChatEffect>()
    val effects = _effects.receiveAsFlow()

    private val selectedChatId = MutableStateFlow<String?>(null)

    private val _state = MutableStateFlow(ManageChatState())
    val state = selectedChatId
        .flatMapLatest { chatId ->
            if (chatId != null) chatRepository.getActiveParticipantsByChatId(chatId)
            else emptyFlow()
        }
        .let { participantsFlow ->
            combine(
                participantsFlow,
                _state,
                sessionRepository.authState.filterIsInstance<AuthState.Authenticated>(),
            ) { participants, currentState, authInfo ->
                currentState.mapExistingParticipantsWithLocalUser(
                    participants = participants,
                    localUserId = authInfo.user.id,
                )
            }
        }
        .stateInUi(ManageChatState())

    private val searchFlow = snapshotFlow { _state.value.queryTextState.text.toString() }
        .debounce(DEFAULT_SEARCH_DEBOUNCE_SECONDS.seconds)
        .onEach(::performSearch)

    init {
        searchFlow.launchIn(viewModelScope)
    }

    fun onAction(action: ManageChatAction) {
        when (action) {
            is ManageChatAction.Submit -> addParticipantsToChat()
            is ManageChatAction.SelectParticipant -> selectParticipant(action.participant)
            is ManageChatAction.RemoveSelectedParticipant -> removeSelectedParticipant(action.participant)
            is ManageChatAction.SelectChat -> selectedChatId.update { action.chatId }
        }
    }

    private fun addParticipantsToChat() {
        if (state.value.selectedChatParticipants.isEmpty()) return

        val chatId = selectedChatId.value ?: return

        val selectedParticipants = state.value.selectedChatParticipants
        val selectedUserIds = selectedParticipants.map { it.id }

        viewModelScope.launch {
            chatRepository
                .addParticipantsToChat(
                    chatId = chatId,
                    userIds = selectedUserIds,
                )
                .onSuccess {
                    _effects.send(ManageChatEffect.MembersAdded)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isSubmitting = false,
                            submitError = error.asUiText(),
                        )
                    }
                }
        }
    }

    private fun removeSelectedParticipant(participant: ChatParticipantUi) {
        viewModelScope.launch {
            val newSelectedParticipants = state.value.selectedChatParticipants - participant
            val isAlreadySelected = participant in newSelectedParticipants
            _state.update { latestState ->
                latestState.copy(
                    selectedChatParticipants = newSelectedParticipants,
                    currentSearchResult = latestState.currentSearchResult.copy(
                        status = latestState.currentSearchResult.status.takeUnless {
                            isAlreadySelected.not() && latestState.currentSearchResult.status == Status.AlreadySelected
                        }.getOrNew(),
                    ),
                    searchError = latestState.searchError
                        .takeUnless { latestState.currentSearchResult.status == Status.AlreadySelected },
                )
            }
        }
    }

    private fun selectParticipant(newParticipant: ChatParticipantUi) {
        _state.update { latestState ->
            latestState.copy(
                selectedChatParticipants = latestState.selectedChatParticipants + newParticipant,
                currentSearchResult = CurrentSearchResultState.Empty,
                searchError = null,
            )
        }
        state.value.queryTextState.clearText()
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            _state.update {
                it.copy(
                    currentSearchResult = CurrentSearchResultState.Empty,
                    searchError = null,
                )
            }
            return
        }

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isSearching = true,
                )
            }

            chatParticipantRepository
                .searchParticipant(query)
                .onSuccess { participant ->
                    val isAlreadyExists =
                        state.value.existingChatParticipants.any { it.id == participant.userId }

                    val isAlreadySelected =
                        state.value.selectedChatParticipants.any { it.id == participant.userId }

                    val searchError = when {
                        isAlreadyExists -> Res.string.participant_is_already_exists.asUiText()
                        isAlreadySelected -> Res.string.participant_is_already_selected.asUiText()
                        else -> null
                    }

                    _state.update {
                        it.copy(
                            currentSearchResult = CurrentSearchResultState(
                                participant = participant.toUi(),
                                status = when {
                                    isAlreadyExists -> Status.AlreadyExists
                                    isAlreadySelected -> Status.AlreadySelected
                                    else -> Status.New
                                },
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
                    _state.update {
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

private fun ManageChatState.mapExistingParticipantsWithLocalUser(
    participants: List<ChatParticipant>,
    localUserId: String,
): ManageChatState {
    val (localUser, otherParticipants) = participants.partition { it.userId == localUserId }
    return this.copy(
        existingChatParticipants = otherParticipants.map(ChatParticipant::toUi),
        localUser = localUser.firstOrNull()?.toUi(),
    )
}
