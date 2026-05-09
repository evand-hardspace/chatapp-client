package com.evandhardspace.chat.presentation.chat_list_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.domain.repository.ChatConnectionRepository
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class SharedChatListDetailsViewModel(
    private val connectionRepository: ChatConnectionRepository,
) : ViewModel() {

    val state: StateFlow<SharedChatListDetailsState>
        field = MutableStateFlow(SharedChatListDetailsState())

    init {
        connectionRepository.chatMessages.launchIn(viewModelScope)
    }
    fun onAction(action: SharedChatListDetailsAction) {
        when (action) {
            is SharedChatListDetailsAction.OpenChat -> {
                state.update {
                    it.copy(
                        selectedChatId = action.chatId,
                        dialogState = DialogState.Hidden,
                    )
                }
            }

            is SharedChatListDetailsAction.CloseChat -> {
                state.update {
                    it.copy(
                        selectedChatId = null,
                    )
                }
            }

            is SharedChatListDetailsAction.CreateChat -> {
                state.update {
                    it.copy(
                        dialogState = DialogState.CreateChat,
                    )
                }
            }

            is SharedChatListDetailsAction.DismissCurrentDialog -> {
                state.update {
                    it.copy(
                        dialogState = DialogState.Hidden,
                    )
                }
            }

            is SharedChatListDetailsAction.ManageChat -> {
                state.value.selectedChatId?.let { id ->
                    state.update {
                        it.copy(
                            dialogState = DialogState.ManageChat(id),
                        )
                    }
                }
            }

            is SharedChatListDetailsAction.OpenProfileSettings -> {
                state.update {
                    it.copy(
                        dialogState = DialogState.Profile,
                    )
                }
            }
        }
    }
}
