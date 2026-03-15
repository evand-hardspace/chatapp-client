package com.evandhardspace.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.presentation.temporaryChatListState
import com.evandhardspace.core.domain.auth.SessionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ChatListViewModel(
    private val sessionStorage: SessionRepository,
) : ViewModel() {

    val state: StateFlow<ChatListState>
        field = MutableStateFlow(ChatListState())

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                state.update { temporaryChatListState() }
            }
        }
    }

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnDismissLogoutDialog -> Unit
            is ChatListAction.OnDismissUserMenu -> Unit
            is ChatListAction.OnLogout -> Unit
            is ChatListAction.OnUserAvatar -> Unit
        }
    }
}
