package com.evandhardspace.auth.presentation.register_success

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute.RegisterSuccess.Companion.EMAIL_ARG_KEY
import com.evandhardspace.core.domain.auth.AuthService
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class RegisterSuccessViewModel(
    private val authService: AuthService,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val email: String = requireNotNull(savedStateHandle[EMAIL_ARG_KEY])

    private val _effects = Channel<RegisterSuccessEffect>()
    val effects = _effects.receiveAsFlow()

    private val _state = MutableStateFlow(
        RegisterSuccessState(registeredEmail = email),
    )
    val state = _state.asStateFlow()


    fun onAction(action: RegisterSuccessAction) {
        when (action) {
            is RegisterSuccessAction.OnLogin -> Unit // TODO
            is RegisterSuccessAction.OnResendVerificationEmail -> resendVerification()
        }
    }

    private fun resendVerification() {
        if (state.value.isResendingVerificationEmail) return

        viewModelScope.launch {
            _state.update {
                it.copy(
                    isResendingVerificationEmail = true,
                )
            }

            authService
                .resendVerificationEmail(email)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isResendingVerificationEmail = false,
                        )
                    }
                    _effects.send(RegisterSuccessEffect.ResendVerificationEmailSuccess)
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            isResendingVerificationEmail = false,
                            resendVerificationError = error.asUiText(),
                        )
                    }
                }
        }
    }
}
