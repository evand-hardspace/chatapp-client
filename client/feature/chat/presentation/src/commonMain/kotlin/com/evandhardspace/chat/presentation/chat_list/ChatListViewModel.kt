package com.evandhardspace.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.domain.repository.ChatConnectionRepository
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.domain.usercase.LogoutUseCase
import com.evandhardspace.chat.presentation.mapper.toUi
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ChatListViewModel(
    private val repository: ChatRepository,
    sessionRepository: SessionRepository,
    private val chatConnectionRepository: ChatConnectionRepository,
    private val logoutUseCase: LogoutUseCase,
) : ViewModel() {

    private val _effects = Channel<ChatListEffect>()
    val effects = _effects.receiveAsFlow()

    val state: StateFlow<ChatListState>
        field = MutableStateFlow(ChatListState())

    init {
        combine(
            repository.chats,
            sessionRepository.user,
        ) { chats, user ->
            state.update {
                it.copy(
                    chats = chats.map { chat -> chat.toUi(user.id) },
                    localParticipant = user.toUi(),
                )
            }
        }
            .launchIn(viewModelScope)

        loadChats()
        observeConnectionState()
    }

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.DismissLogoutDialog -> state.update { latestState ->
                latestState.copy(
                    showLogoutConfirmation = false,
                )
            }

            is ChatListAction.DismissUserMenu -> state.update { latestState ->
                latestState.copy(
                    isUserMenuOpen = false,
                )
            }

            is ChatListAction.Logout -> logout()
            is ChatListAction.OpenLogout -> showLogoutConfirmation()
            is ChatListAction.OpenUserMenu -> state.update { latestState ->
                latestState.copy(
                    isUserMenuOpen = true,
                )
            }

            is ChatListAction.SelectChat -> {
                state.update { latestState ->
                    latestState.copy(selectedChatId = action.chatId)
                }
            }
        }
    }

    private fun showLogoutConfirmation() {
        state.update { latestState ->
            latestState.copy(
                showLogoutConfirmation = true,
            )
        }
    }

    private fun logout() {
        state.update { latestState ->
            latestState.copy(
                showLogoutConfirmation = false,
            )
        }

        viewModelScope.launch {
            logoutUseCase().onFailure { error ->
                _effects.send(
                    ChatListEffect.LoggedOutFailed(
                        error = error.asUiText(),
                    )
                )
            }
        }
    }

    private fun loadChats() {
        viewModelScope.launch {
            repository.fetchChats()
        }
    }

    private fun observeConnectionState() {
        chatConnectionRepository
            .connectionState
            .launchIn(viewModelScope)
    }
}
