package com.evandhardspace.auth.presentation.register

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.error_account_exists
import chatapp.feature.auth.presentation.generated.resources.error_invalid_email
import chatapp.feature.auth.presentation.generated.resources.error_invalid_password
import chatapp.feature.auth.presentation.generated.resources.error_invalid_username
import com.evandhardspace.auth.domain.validation.EmailValidator
import com.evandhardspace.auth.domain.validation.UsernameValidator
import com.evandhardspace.core.domain.auth.AuthService
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.domain.validation.rule.password.PasswordValidator
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

class RegisterViewModel(
    private val emailValidator: EmailValidator,
    private val passwordValidator: PasswordValidator,
    private val usernameValidator: UsernameValidator,
    private val authService: AuthService,
) : ViewModel() {

    private val _events = Channel<RegisterEffect>()
    val events = _events.receiveAsFlow()

    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val emailErrorFlow = snapshotFlow { state.value.emailTextState.text.toString() }
        .map { email ->
            val isEmailValid = emailValidator.validate(email)
            if (isEmailValid.not()) Res.string.error_invalid_email.asUiText()
            else null
        }
        .distinctUntilChanged()

    private val usernameErrorFlow = snapshotFlow { state.value.usernameTextState.text.toString() }
        .map { username ->
            val isUsernameValid = usernameValidator.validate(username)
            if (isUsernameValid.not()) Res.string.error_invalid_username.asUiText()
            else null
        }
        .distinctUntilChanged()

    private val passwordErrorFlow = snapshotFlow { state.value.passwordTextState.text.toString() }
        .map { password ->
            val isPasswordValid = passwordValidator.validate(password)
            if (isPasswordValid.not()) Res.string.error_invalid_password.asUiText()
            else null
        }
        .distinctUntilChanged()

    init {
        combine(
            emailErrorFlow,
            usernameErrorFlow,
            passwordErrorFlow,
        ) { emailError, usernameError, passwordError ->
            _state.update {
                it.copy(
                    emailError = emailError,
                    usernameError = usernameError,
                    passwordError = passwordError,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnLoginClick -> login()
            is RegisterAction.OnRegisterClick -> register()
            is RegisterAction.OnInputTextFocusGain -> Unit
            is RegisterAction.OnTogglePasswordVisibilityClick -> _state.update {
                it.copy(
                    isPasswordVisible = it.isPasswordVisible.not(),
                )
            }
        }
    }

    private fun register() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isRegistering = true
                )
            }

            val email = state.value.emailTextState.text.toString()
            val username = state.value.usernameTextState.text.toString()
            val password = state.value.passwordTextState.text.toString()

            authService
                .register(
                    email = email,
                    username = username,
                    password = password,
                )
                .onSuccess {
                    _state.update {
                        it.copy(
                            isRegistering = false
                        )
                    }
                }
                .onFailure { error ->
                    val registrationError = when (error) {
                        DataError.Remote.Conflict -> Res.string.error_account_exists.asUiText()
                        else -> error.asUiText()
                    }
                    _state.update {
                        it.copy(
                            isRegistering = false,
                            registrationError = registrationError,
                        )
                    }
                }
        }
    }
    private fun login() {
        // TODO: implement login
    }
}
