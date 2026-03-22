package com.evandhardspace.chat.presentation.manage_chat

import androidx.lifecycle.ViewModel
import com.evandhardspace.chat.presentation.component.manage_chat.ManageChatAction
import com.evandhardspace.chat.presentation.component.manage_chat.ManageChatState
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class ManageChatViewModel : ViewModel() {
    private val _effects = Channel<ManageChatEffect>()
    val effects = _effects.receiveAsFlow()

    val state: StateFlow<ManageChatState>
        field = MutableStateFlow(ManageChatState())

    fun onAction(action: ManageChatAction) {
        when (action) {
            is ManageChatAction.Submit -> {}
            is ManageChatAction.SelectParticipant -> {}
            is ManageChatAction.RemoveSelectedParticipant -> {}
        }
    }
}