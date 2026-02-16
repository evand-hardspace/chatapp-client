package com.evandhardspace.auth.presentation.email_verifiaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.auth.presentation.email_verifiaction.EmailVerificationState.VerificationState
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute.EmailVerification
import com.evandhardspace.core.domain.auth.AuthService
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class EmailVerificationViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val token: String? = savedStateHandle[EmailVerification.TOKEN_ARG_KEY]

    private val _state = MutableStateFlow(EmailVerificationState())
    val state = _state.asStateFlow()

    init {
        viewModelScope.launch {
            verifyEmail()
        }
    }

    fun onAction(action: EmailVerificationAction) {
        when (action) {
            EmailVerificationAction.OnClose -> Unit
            EmailVerificationAction.OnLogin -> Unit
        }
    }

    private fun verifyEmail() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    verification = VerificationState.Verifying,
                )
            }

            if (token == null) {
                _state.update {
                    it.copy(verification = VerificationState.Error)
                }
                return@launch
            }

            authService.verifyEmail(token)
                .onSuccess {
                    _state.update { it.copy(verification = VerificationState.Verified) }
                }
                .onFailure {
                    _state.update { it.copy(verification = VerificationState.Error) }
                }
        }
    }
}
