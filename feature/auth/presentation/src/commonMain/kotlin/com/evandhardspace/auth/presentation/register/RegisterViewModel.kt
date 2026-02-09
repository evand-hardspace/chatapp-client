package com.evandhardspace.auth.presentation.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.error_account_exists
import chatapp.feature.auth.presentation.generated.resources.error_invalid_email
import chatapp.feature.auth.presentation.generated.resources.error_invalid_password
import chatapp.feature.auth.presentation.generated.resources.error_invalid_username
import com.evandhardspace.auth.domain.validation.EmailValidator
import com.evandhardspace.auth.domain.validation.UsernameValidator
import com.evandhardspace.core.common.FlowInitOwner
import com.evandhardspace.core.common.onInit
import com.evandhardspace.core.common.stateInUi
import com.evandhardspace.core.domain.auth.AuthService
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.domain.validation.rule.password.PasswordValidator
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val emailValidator: EmailValidator = EmailValidator(),
    private val passwordValidator: PasswordValidator = PasswordValidator(),
    private val usernameValidator: UsernameValidator = UsernameValidator(),
    private val authService: AuthService,
) : ViewModel() {

    private val _events = Channel<RegisterEffect>()
    val events = _events.receiveAsFlow()

    private val initOwner = FlowInitOwner()
    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onInit(initOwner) {
            // load state
        }
        .stateInUi(RegisterState())

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnLoginClick -> validateFormInputs()
            is RegisterAction.OnRegisterClick -> register()
            is RegisterAction.OnInputTextFocusGain -> Unit
            is RegisterAction.OnTogglePasswordVisibilityClick -> Unit
        }
    }

    private fun validateFormInputs(): Boolean {
        clearAllTextFieldErrors()

        val currentState = state.value
        val email = currentState.emailTextState.text
        val username = currentState.usernameTextState.text
        val password = currentState.passwordTextState.text

        val isEmailValid = emailValidator.validate(email)
        val isPasswordValid = passwordValidator.validate(password)
        val isUsernameValid = usernameValidator.validate(username)

        val emailError = if (isEmailValid.not()) {
            Res.string.error_invalid_email.asUiText()
        } else null
        val usernameError = if (isUsernameValid.not()) {
            Res.string.error_invalid_username.asUiText()
        } else null
        val passwordError = if (isPasswordValid.not()) {
            Res.string.error_invalid_password.asUiText()
        } else null

        _state.update {
            it.copy(
                emailError = emailError,
                usernameError = usernameError,
                passwordError = passwordError,
            )
        }

        return isUsernameValid && isEmailValid && isPasswordValid
    }

    private fun register() {
        if (validateFormInputs()) return

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

    private fun clearAllTextFieldErrors() {
        _state.update {
            it.copy(
                emailError = null,
                usernameError = null,
                passwordError = null,
                registrationError = null,
            )
        }
    }
}
