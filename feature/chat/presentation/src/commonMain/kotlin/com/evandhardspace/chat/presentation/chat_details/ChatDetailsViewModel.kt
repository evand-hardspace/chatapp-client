package com.evandhardspace.chat.presentation.chat_details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.presentation.temporaryChatDetailsState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ChatDetailsViewModel : ViewModel() {

    val state: StateFlow<ChatDetailsState>
        field = MutableStateFlow(ChatDetailsState())

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                state.update { temporaryChatDetailsState() }
            }
        }
    }

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
            is ChatDetailsAction.OnSelectChat -> Unit
            is ChatDetailsAction.OnSendMessage -> Unit
        }
    }
}
