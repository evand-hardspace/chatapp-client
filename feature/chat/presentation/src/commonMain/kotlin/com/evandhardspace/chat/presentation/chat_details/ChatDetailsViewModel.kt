package com.evandhardspace.chat.presentation.chat_details

import androidx.compose.foundation.text.input.clearText
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.presentation.mapper.toUi
import com.evandhardspace.core.common.stateInUi
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ChatDetailsViewModel(
    private val chatRepository: ChatRepository,
    sessionRepository: SessionRepository,
) : ViewModel() {

    private val _effects = Channel<ChatDetailsEffect>()
    val effects = _effects.receiveAsFlow()

    private val selectedChatId = MutableStateFlow<String?>(null)

    private val chatInfoFlow = selectedChatId
        .flatMapLatest { chatId ->
            if (chatId != null) chatRepository.getChatInfoById(chatId)
            else emptyFlow()
        }

    private val _state = MutableStateFlow(ChatDetailsState())

    private val stateWithMessages: Flow<ChatDetailsState> = combine(
        _state,
        chatInfoFlow,
        sessionRepository.authState,
    ) { currentState, chatInfo, authState ->
        if (authState !is AuthState.Authenticated) return@combine ChatDetailsState()

        currentState.copy(
            chatUi = chatInfo.chat.toUi(authState.user.id),
        )
    }

    val state = selectedChatId
        .flatMapLatest { chatId ->
            if (chatId != null) stateWithMessages
            else _state
        }
        .stateInUi(ChatDetailsState())

    fun onAction(action: ChatDetailsAction) {
        when (action) {
            is ChatDetailsAction.OnChatMembersSelected -> Unit
            is ChatDetailsAction.OnChatOptions -> onChatOptions()
            is ChatDetailsAction.OnDeleteMessage -> Unit
            is ChatDetailsAction.OnDismissChatOptions -> onDismissChatOptions()
            is ChatDetailsAction.OnDismissMessageMenu -> Unit
            is ChatDetailsAction.LeaveChat -> onLeaveChat()
            is ChatDetailsAction.OnMessageLongClick -> Unit
            is ChatDetailsAction.OnRetry -> Unit
            is ChatDetailsAction.OnScrollToTop -> Unit
            is ChatDetailsAction.OnSelectChat -> switchChat(action.chatId)
            is ChatDetailsAction.OnSendMessage -> Unit
        }
    }

    private fun switchChat(chatId: String?) {
        selectedChatId.update { chatId }
        viewModelScope.launch {
            chatId?.let {
                chatRepository.fetchChatById(chatId)
            }
        }
    }

    private fun onChatOptions() {
        _state.update {
            it.copy(isChatOptionsOpen = true)
        }
    }

    private fun onDismissChatOptions() {
        _state.update {
            it.copy(isChatOptionsOpen = false)
        }
    }

    private fun onLeaveChat() {
        val chatId = selectedChatId.value ?: return

        _state.update {
            it.copy(isChatOptionsOpen = false)
        }

        viewModelScope.launch {
            chatRepository.leaveChat(chatId)
                .onSuccess {
                    _state.value.messageTextFieldState.clearText()

                    selectedChatId.update { null }

                    _state.update { it.copy(
                        chatUi = null,
                        messages = emptyList(),
                        bannerState = BannerState(),
                    ) }
                    _effects.send(
                        ChatDetailsEffect.ChatLeft,
                    )
                }
                .onFailure { error ->
                    _effects.send(
                        ChatDetailsEffect.Error(
                            error = error.asUiText(),
                        ),
                    )
                }
        }
    }
}
