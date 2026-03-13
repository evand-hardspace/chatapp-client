@file:OptIn(FlowPreview::class)

package com.evandhardspace.chat.presentation.create_chat

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.error_participant_not_found
import com.evandhardspace.chat.domain.ChatParticipantRepository
import com.evandhardspace.chat.presentation.create_chat.mapper.toUi
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import kotlin.time.Duration.Companion.seconds

private const val DEFAULT_SEARCH_DEBOUNCE_SECONDS = 1

@KoinViewModel
internal class CreateChatViewModel(
    private val chatParticipantRepository: ChatParticipantRepository,
) : ViewModel() {

    private val searchFlow = snapshotFlow { state.value.queryTextState.text.toString() }
        .debounce(DEFAULT_SEARCH_DEBOUNCE_SECONDS.seconds)
        .onEach { query ->
            performSearch(query)
        }

    val state: StateFlow<CreateChatState>
        field = MutableStateFlow(CreateChatState())

    init {
        searchFlow.launchIn(viewModelScope)
    }

    fun onAction(action: CreateChatAction) {
        when (action) {
            CreateChatAction.OnAdd -> addParticipant()
            CreateChatAction.OnCreateChat -> Unit
        }
    }

    private fun addParticipant() {
        state.value.currentSearchResult?.let { participant ->
            val isAlreadyPartOfChat = state.value.selectedChatParticipants.any {
                it.id == participant.id
            }
            if (!isAlreadyPartOfChat) {
                state.update {
                    it.copy(
                        selectedChatParticipants = it.selectedChatParticipants + participant,
                        canAddParticipant = false,
                        currentSearchResult = null
                    )
                }
                state.value.queryTextState.clearText()
            }
        }
    }

    private fun performSearch(query: String) {
        if (query.isBlank()) {
            state.update {
                it.copy(
                    currentSearchResult = null,
                    canAddParticipant = false,
                    searchError = null,
                )
            }
            return
        }

        viewModelScope.launch {
            state.update {
                it.copy(
                    isSearching = true,
                    canAddParticipant = false,
                )
            }

            chatParticipantRepository
                .searchParticipant(query)
                .onSuccess { participant ->
                    state.update {
                        it.copy(
                            currentSearchResult = participant.toUi(),
                            isSearching = false,
                            canAddParticipant = true,
                            searchError = null
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        DataError.Remote.NotFound -> Res.string.error_participant_not_found.asUiText()
                        else -> error.asUiText()
                    }
                    state.update {
                        it.copy(
                            searchError = errorMessage,
                            isSearching = false,
                            canAddParticipant = false,
                            currentSearchResult = null,
                        )
                    }
                }
        }
    }
}
