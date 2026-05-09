package com.evandhardspace.chatapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.auth.SessionEvents
import dev.zacsweers.metro.AppScope
import dev.zacsweers.metro.ContributesIntoMap
import dev.zacsweers.metro.Inject
import dev.zacsweers.metrox.viewmodel.ViewModelKey
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

@Inject
@ViewModelKey
@ContributesIntoMap(AppScope::class)
class MainViewModel(
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _effects = Channel<MainEffect>()
    val effects = _effects.receiveAsFlow()

    val state: StateFlow<MainState>
        field = MutableStateFlow<MainState>(MainState.Loading)

    init {
        loadLoggedInInitialState()
        observeLoggedOutEvent()
    }

    private fun loadLoggedInInitialState() {
        viewModelScope.launch {
            delay(1000)
            val authState = sessionRepository.authState.first()
            state.update {
                MainState.Loaded(
                    isAuthorized = authState is AuthState.Authenticated,
                )
            }
        }
    }

    private fun observeLoggedOutEvent() {
        sessionRepository.events
            .filterIsInstance<SessionEvents.LoggedOut>()
            .onEach {
                state.update { MainState.Loaded(isAuthorized = false) }
                _effects.send(MainEffect.LoggedOut)
            }.launchIn(viewModelScope)
    }
}
