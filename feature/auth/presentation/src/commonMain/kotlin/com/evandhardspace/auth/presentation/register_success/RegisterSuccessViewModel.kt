package com.evandhardspace.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute.RegisterSuccess.Companion.EMAIL_ARG_KEY
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class RegisterSuccessViewModel(
    private val authRepository: AuthRepository,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val email: String = requireNotNull(savedStateHandle[EMAIL_ARG_KEY])

    private val _effects = Channel<RegisterSuccessEffect>()
    val effects = _effects.receiveAsFlow()

    val state: StateFlow<RegisterSuccessState>
        field = MutableStateFlow(
            RegisterSuccessState(
                registeredEmail = email,
            ),
        )

    fun onAction(action: RegisterSuccessAction) {
        when (action) {
            is RegisterSuccessAction.OnResendVerificationEmail -> resendVerification()
        }
    }

    private fun resendVerification() {
        if (state.value.isResendingVerificationEmail) return

        viewModelScope.launch {
            state.update {
                it.copy(
                    isResendingVerificationEmail = true,
                )
            }

            authRepository
                .resendVerificationEmail(email)
                .onSuccess {
                    state.update {
                        it.copy(
                            isResendingVerificationEmail = false,
                        )
                    }
                    _effects.send(RegisterSuccessEffect.ResendVerificationEmailSuccess)
                }
                .onFailure { error ->
                    state.update {
                        it.copy(
                            isResendingVerificationEmail = false,
                            resendVerificationError = error.asUiText(),
                        )
                    }
                }
        }
    }
}
