package com.evandhardspace.auth.presentation.register

import androidx.lifecycle.ViewModel
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.error_invalid_email
import chatapp.feature.auth.presentation.generated.resources.error_invalid_password
import chatapp.feature.auth.presentation.generated.resources.error_invalid_username
import com.evandhardspace.auth.domain.validation.EmailValidator
import com.evandhardspace.auth.domain.validation.UsernameValidator
import com.evandhardspace.core.common.InitOwner
import com.evandhardspace.core.common.onInit
import com.evandhardspace.core.common.stateInUi
import com.evandhardspace.core.domain.validation.rule.password.PasswordValidator
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class RegisterViewModel(
    private val emailValidator: EmailValidator = EmailValidator(),
    private val passwordValidator: PasswordValidator = PasswordValidator(),
    private val usernameValidator: UsernameValidator = UsernameValidator(),
) : ViewModel() {

    private val initOwner = InitOwner()

    private val _state = MutableStateFlow(RegisterState())
    val state = _state
        .onInit(initOwner) {
            // load state
        }
        .stateInUi(RegisterState())

    fun onAction(action: RegisterAction) {
        when (action) {
            is RegisterAction.OnLoginClick -> validateFormInputs()
            is RegisterAction.OnInputTextFocusGain -> Unit
            is RegisterAction.OnRegisterClick -> Unit
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
