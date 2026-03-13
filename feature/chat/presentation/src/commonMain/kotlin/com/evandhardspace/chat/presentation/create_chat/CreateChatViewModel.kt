package com.evandhardspace.chat.presentation.create_chat

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class CreateChatViewModel : ViewModel() {

    val state: StateFlow<CreateChatState>
        field = MutableStateFlow(CreateChatState())

    fun onAction(action: CreateChatAction) {
        when (action) {
            CreateChatAction.OnAdd -> Unit
            CreateChatAction.OnCreateChat -> Unit
            CreateChatAction.OnDismissDialog -> Unit
        }
    }
}
