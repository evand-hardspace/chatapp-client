package com.evandhardspace.chatapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.auth.SessionEvents
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
class MainViewModel(
    private val sessionRepository: MutableSessionRepository,
) : ViewModel() {

    private val _effects = Channel<MainEffect>()
    val effects = _effects.receiveAsFlow()

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    val state = _state.asStateFlow()

    init {
        loadLoggedInInitialState()
        observeLoggedOutEvent()
    }

    private fun loadLoggedInInitialState() {
        viewModelScope.launch {
            val authState = sessionRepository.authState.first()
            _state.update {
                MainState.Loaded(
                    isLoggedIn = authState is AuthState.Authorized,
                )
            }
        }
    }

    private fun observeLoggedOutEvent() {
        sessionRepository.events
            .filterIsInstance<SessionEvents.LoggedOut>()
            .onEach {
                _state.update { MainState.Loaded(isLoggedIn = false) }
                _effects.send(MainEffect.LoggedOut)
            }.launchIn(viewModelScope)
    }
}