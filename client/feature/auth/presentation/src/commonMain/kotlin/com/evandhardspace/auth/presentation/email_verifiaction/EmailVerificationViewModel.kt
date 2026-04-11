package com.evandhardspace.auth.presentation.email_verifiaction

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.auth.presentation.email_verifiaction.EmailVerificationState.VerificationState
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute.EmailVerification
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class EmailVerificationViewModel(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val token: String? = savedStateHandle[EmailVerification.TOKEN_ARG_KEY]

    val state: StateFlow<EmailVerificationState>
        field = MutableStateFlow(EmailVerificationState())

    init {
        viewModelScope.launch {
            verifyEmail()
        }
    }

    private fun verifyEmail() {
        viewModelScope.launch {
            state.update {
                it.copy(
                    verification = VerificationState.Verifying,
                )
            }
            val isAuthenticated = sessionRepository.authState.first() is AuthState.Authenticated

            if (token == null) {
                state.update {
                    it.copy(verification = VerificationState.Error(isAuthenticated))
                }
                return@launch
            }

            authRepository.verifyEmail(token)
                .onSuccess {
                    state.update {
                        it.copy(
                            verification = VerificationState.Verified(
                                isAuthenticated = isAuthenticated,
                            ),
                        )
                    }
                }
                .onFailure {
                    state.update { it.copy(verification = VerificationState.Error(isAuthenticated)) }
                }
        }
    }
}
