package com.evandhardspace.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.presentation.mapper.toUi
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ChatListViewModel(
    private val repository: ChatRepository,
    sessionRepository: SessionRepository,
) : ViewModel() {

    val state: StateFlow<ChatListState>
        field = MutableStateFlow(ChatListState())

    init {
        combine(
            repository.chats,
            sessionRepository.authState,
        ) { chats, authState ->
            if (authState !is AuthState.Authenticated) {
                return@combine ChatListState()
            }

            state.update {
                it.copy(
                    chats = chats.map { chat -> chat.toUi(authState.user.id) },
                    localParticipant = authState.user.toUi(),
                )
            }
        }
            .launchIn(viewModelScope)

        loadChats()
    }

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.DismissLogoutDialog -> Unit
            is ChatListAction.DismissUserMenu -> Unit
            is ChatListAction.Logout -> Unit
            is ChatListAction.OnUserAvatar -> Unit
            is ChatListAction.SelectChat -> {
                state.update {
                    it.copy(selectedChatId = action.chatId)
                }
            }
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            repository.fetchChats()
        }
    }
}
