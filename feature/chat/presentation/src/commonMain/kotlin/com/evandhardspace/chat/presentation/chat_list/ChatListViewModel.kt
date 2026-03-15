package com.evandhardspace.chat.presentation.chat_list

import androidx.lifecycle.ViewModel
import com.evandhardspace.core.domain.auth.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ChatListViewModel(
    private val sessionStorage: SessionRepository,
): ViewModel() {

    val state: StateFlow<ChatListState>
    field = MutableStateFlow(ChatListState())

    fun onAction(action: ChatListAction) {
        when (action) {
            is ChatListAction.OnDismissLogoutDialog -> Unit
            is ChatListAction.OnDismissUserMenu -> Unit
            is ChatListAction.OnLogout -> Unit
            is ChatListAction.OnUserAvatar -> Unit
        }
    }
}
