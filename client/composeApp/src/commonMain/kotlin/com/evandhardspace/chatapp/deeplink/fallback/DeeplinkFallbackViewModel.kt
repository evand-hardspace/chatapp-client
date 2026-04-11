package com.evandhardspace.chatapp.deeplink.fallback

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class DeeplinkFallbackViewModel(
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    val state: StateFlow<DeeplinkFallbackState>
        field = MutableStateFlow<DeeplinkFallbackState>(DeeplinkFallbackState.Loading)

    init {
        viewModelScope.launch {
            state.update {
                DeeplinkFallbackState.Loaded(
                    isAuthorized = sessionRepository.authState.first() is AuthState.Authenticated,
                )
            }
        }
    }
}