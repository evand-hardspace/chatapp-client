package com.evandhardspace.auth.presentation.login

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.error_email_not_verified
import chatapp.feature.auth.presentation.generated.resources.error_invalid_credentials
import chatapp.feature.auth.presentation.generated.resources.error_invalid_email
import chatapp.feature.auth.presentation.generated.resources.error_password_should_not_be_empty
import com.evandhardspace.auth.domain.validation.EmailValidator
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class LoginViewModel(
    private val emailValidator: EmailValidator,
    private val authRepository: AuthRepository,
    private val sessionRepository: MutableSessionRepository,
) : ViewModel() {

    private val _effects = Channel<LoginEffect>()
    val effects = _effects.receiveAsFlow()

    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val emailErrorFlow = snapshotFlow { state.value.emailTextFieldState.text.toString() }
        .map { email ->
            val isEmailValid = emailValidator.validate(email)
            if (isEmailValid.not()) Res.string.error_invalid_email.asUiText()
            else null
        }
        .distinctUntilChanged()

    private val passwordErrorFlow =
        snapshotFlow { state.value.passwordTextFieldState.text.toString() }
            .map { password ->
                val isPasswordValid = password.isNotBlank()
                if (isPasswordValid.not()) Res.string.error_password_should_not_be_empty.asUiText()
                else null
            }
            .distinctUntilChanged()

    init {
        combine(
            emailErrorFlow,
            passwordErrorFlow,
        ) { emailError, passwordError ->
            _state.update {
                it.copy(
                    emailError = emailError,
                    passwordError = passwordError,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: LoginAction) {
        when (action) {
            LoginAction.OnLogin -> login()
            LoginAction.OnTogglePasswordVisibility -> togglePasswordVisibility()
            LoginAction.OnForgotPassword -> Unit
            LoginAction.OnSignUp -> Unit
        }
    }

    private fun togglePasswordVisibility() {
        _state.update { it.copy(isPasswordVisible = it.isPasswordVisible.not()) }
    }

    private fun login() {
        if (!state.value.canLogin) {
            return
        }

        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }

            val email = state.value.emailTextFieldState.text.toString()
            val password = state.value.passwordTextFieldState.text.toString()

            authRepository
                .login(
                    email = email,
                    password = password
                )
                .onSuccess { authInfo ->
                    sessionRepository.saveAuthInfo(authInfo)
                    _state.update { it.copy(isLoading = false) }
                    _effects.send(LoginEffect.Success)
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        DataError.Remote.Unauthorized -> Res.string.error_invalid_credentials.asUiText()
                        DataError.Remote.Forbidden -> Res.string.error_email_not_verified.asUiText()
                        else -> error.asUiText()
                    }
                    _state.update {
                        it.copy(
                            error = errorMessage,
                            isLoading = false,
                        )
                    }
                }
        }
    }
}
