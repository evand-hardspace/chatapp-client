package com.evandhardspace.auth.presentation.forgot_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.auth.domain.validation.EmailValidator
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ForgotPasswordViewModel(
    private val authRepository: AuthRepository,
    private val emailValidator: EmailValidator,
) : ViewModel() {

    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    private val isEmailValidFlow = snapshotFlow { state.value.emailTextFieldState.text }
        .map { email -> emailValidator.validate(email) }
        .distinctUntilChanged()

    init {
        observeValidationState()
    }

    fun onAction(action: ForgotPasswordAction) {
        when (action) {
            ForgotPasswordAction.OnSubmitClick -> submitForgotPasswordRequest()
        }
    }

    private fun observeValidationState() {
        isEmailValidFlow.onEach { isEmailValid ->
            _state.update {
                it.copy(
                    canSubmit = isEmailValid,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun submitForgotPasswordRequest() {
        if (state.value.isLoading || state.value.canSubmit.not()) return


        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isEmailSentSuccessfully = false,
                    errorText = null,
                )
            }

            val email = state.value.emailTextFieldState.text.toString()
            authRepository
                .forgotPassword(email)
                .onSuccess {
                    _state.update {
                        it.copy(
                            isEmailSentSuccessfully = true,
                            isLoading = false,
                        )
                    }
                }
                .onFailure { error ->
                    _state.update {
                        it.copy(
                            errorText = error.asUiText(),
                            isLoading = false,
                        )
                    }
                }
        }
    }

}