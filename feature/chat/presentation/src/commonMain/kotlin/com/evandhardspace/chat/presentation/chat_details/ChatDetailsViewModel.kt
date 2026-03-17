package com.evandhardspace.chat.presentation.chat_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.domain.ChatRepository
import com.evandhardspace.chat.presentation.mapper.toUi
import com.evandhardspace.core.common.stateInUi
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ChatDetailsViewModel(
    private val chatRepository: ChatRepository,
    sessionRepository: SessionRepository,
) : ViewModel() {

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
            is ChatDetailsAction.OnChatMembers -> Unit
            is ChatDetailsAction.OnChatOptions -> Unit
            is ChatDetailsAction.OnDeleteMessage -> Unit
            is ChatDetailsAction.OnDismissChatOptions -> Unit
            is ChatDetailsAction.OnDismissMessageMenu -> Unit
            is ChatDetailsAction.OnLeaveChat -> Unit
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
}
