package com.evandhardspace.auth.presentation.reset_password

import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chatapp.feature.auth.presentation.generated.resources.Res
import chatapp.feature.auth.presentation.generated.resources.error_reset_password_token_invalid
import chatapp.feature.auth.presentation.generated.resources.error_same_password
import com.evandhardspace.auth.presentation.navigation.AuthNavGraphRoute.ResetPassword.Companion.TOKEN_ARG_KEY
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.domain.validation.rule.password.PasswordValidator
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

internal class ResetPasswordViewModel(
    private val authRepository: AuthRepository,
    private val passwordValidator: PasswordValidator,
    savedStateHandle: SavedStateHandle,
) : ViewModel() {

    private val token = savedStateHandle.get<String>(TOKEN_ARG_KEY) ?: error("No password reset token")

    private val isPasswordValidFlow = snapshotFlow { state.value.passwordTextState.text }
        .map { password -> passwordValidator.validate(password.toString()) }
        .distinctUntilChanged()

    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state.asStateFlow()

    init {
        observeValidationState()
    }

    fun onAction(action: ResetPasswordAction) {
        when (action) {
            ResetPasswordAction.OnSubmit -> resetPassword()
            ResetPasswordAction.OnTogglePasswordVisibility -> {
                _state.update {
                    it.copy(
                        isPasswordVisible = !it.isPasswordVisible
                    )
                }
            }
        }
    }

    private fun observeValidationState() {
        isPasswordValidFlow.onEach { isPasswordValid ->
            _state.update {
                it.copy(
                    canSubmit = isPasswordValid,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun resetPassword() {
        if (state.value.isLoading || state.value.canSubmit.not()) return


        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true,
                    isPasswordChanged = false,
                )
            }

            val newPassword = state.value.passwordTextState.text.toString()
            authRepository
                .resetPassword(
                    newPassword = newPassword,
                    token = token,
                )
                .onSuccess {
                    _state.update {
                        it.copy(
                            isLoading = false,
                            isPasswordChanged = true,
                            errorText = null,
                        )
                    }
                }
                .onFailure { error ->
                    val errorText = when (error) {
                        DataError.Remote.Unauthorized -> Res.string.error_reset_password_token_invalid.asUiText()
                        DataError.Remote.Conflict -> Res.string.error_same_password.asUiText()
                        else -> error.asUiText()
                    }
                    _state.update {
                        it.copy(
                            errorText = errorText,
                            isLoading = false,
                        )
                    }
                }
        }
    }
}