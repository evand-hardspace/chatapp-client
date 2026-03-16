package com.evandhardspace.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.domain.ChatRepository
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
    private val sessionRepository: SessionRepository,
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
            is ChatListAction.OnDismissLogoutDialog -> Unit
            is ChatListAction.OnDismissUserMenu -> Unit
            is ChatListAction.OnLogout -> Unit
            is ChatListAction.OnUserAvatar -> Unit
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            repository.fetchChats()
        }
    }
}
