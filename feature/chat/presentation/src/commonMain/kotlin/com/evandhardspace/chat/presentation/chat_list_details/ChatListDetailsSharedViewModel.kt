package com.evandhardspace.chat.presentation.chat_list_details

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ChatListDetailsSharedViewModel : ViewModel() {

    val state: StateFlow<ChatListDetailsState>
        field = MutableStateFlow(ChatListDetailsState())

    fun onAction(action: ChatListDetailsAction) {
        when (action) {
            is ChatListDetailsAction.OnOpenChat -> {
                state.update {
                    it.copy(
                        selectedChatId = action.chatId,
                    )
                }
            }

            ChatListDetailsAction.OnCreateChat -> {
                state.update {
                    it.copy(
                        dialogState = DialogState.CreateChat,
                    )
                }
            }

            ChatListDetailsAction.OnDismissCurrentDialog -> {
                state.update {
                    it.copy(
                        dialogState = DialogState.Hidden,
                    )
                }
            }

            ChatListDetailsAction.OnManageChat -> {
                state.value.selectedChatId?.let { id ->
                    state.update {
                        it.copy(
                            dialogState = DialogState.ManageChat(id),
                        )
                    }
                }
            }

            ChatListDetailsAction.OnOpenProfileSettings -> {
                state.update {
                    it.copy(
                        dialogState = DialogState.Profile,
                    )
                }
            }
        }
    }
}
