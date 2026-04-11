package com.evandhardspace.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import chatapp.client.feature.chat.presentation.generated.resources.Res
import chatapp.client.feature.chat.presentation.generated.resources.error_current_password_equal_to_new_one
import chatapp.client.feature.chat.presentation.generated.resources.error_current_password_incorrect
import chatapp.client.feature.chat.presentation.generated.resources.error_invalid_file_type
import com.evandhardspace.chat.domain.repository.ChatParticipantRepository
import com.evandhardspace.core.domain.auth.AuthRepository
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.DataError
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.domain.validation.rule.password.PasswordValidator
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ProfileViewModel(
    private val authRepository: AuthRepository,
    private val passwordValidator: PasswordValidator,
    private val chatParticipantRepository: ChatParticipantRepository,
    private val sessionRepository: SessionRepository,
) : ViewModel() {

    private val _effects = Channel<ProfileEffect>()
    val effects = _effects.receiveAsFlow()

    val state: StateFlow<ProfileState>
        field = MutableStateFlow(ProfileState())

    init {
        observeCanChangePassword()
        observeLocalParticipant()
        fetchLocalParticipantDetails()
    }

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnChangePasswordClick -> changePassword()
            is ProfileAction.OnConfirmDeleteClick -> deleteProfilePicture()
            is ProfileAction.OnDeletePictureClick -> showDeleteConfirmation()
            is ProfileAction.OnDismissDeleteConfirmationDialogClick -> dismissDeleteConfirmation()
            is ProfileAction.PictureSelected -> uploadProfilePicture(action.bytes, action.mimeType)
            is ProfileAction.OnToggleCurrentPasswordVisibility -> toggleCurrentPasswordVisibility()
            is ProfileAction.OnToggleNewPasswordVisibility -> toggleNewPasswordVisibility()
            is ProfileAction.OnUploadPicture -> {
                viewModelScope.launch { _effects.send(ProfileEffect.SelectPicture) }
            }

            is ProfileAction.OnDismiss -> {
                viewModelScope.launch { _effects.send(ProfileEffect.Dismiss) }
            }
        }
    }

    private fun observeCanChangePassword() {
        val isCurrentPasswordValidFlow = snapshotFlow {
            state.value.currentPasswordTextState.text.toString()
        }.map(String::isNotBlank).distinctUntilChanged()

        val isNewPasswordValidFlow = snapshotFlow {
            state.value.newPasswordTextState.text.toString()
        }.map(passwordValidator::validate).distinctUntilChanged()

        combine(
            isCurrentPasswordValidFlow,
            isNewPasswordValidFlow,
        ) { isCurrentValid, isNewValid ->
            state.update { latestState ->
                latestState.copy(
                    canChangePassword = isCurrentValid && isNewValid,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun observeLocalParticipant() {
        viewModelScope.launch {
            sessionRepository.user.collect { user ->
                state.update { latestState ->
                    latestState.copy(
                        username = user.username,
                        userInitials = user.username.take(2),
                        emailTextState = TextFieldState(initialText = user.email),
                        profilePictureUrl = user.profilePictureUrl,
                    )
                }
            }
        }
    }

    private fun toggleCurrentPasswordVisibility() {
        state.update { latestState ->
            latestState.copy(
                isCurrentPasswordVisible = latestState.isCurrentPasswordVisible.not(),
            )
        }
    }

    private fun toggleNewPasswordVisibility() {
        state.update { latestState ->
            latestState.copy(
                isNewPasswordVisible = latestState.isNewPasswordVisible.not(),
            )
        }
    }

    private fun changePassword() {
        val latestState = state.value
        if (latestState.canChangePassword.not() && latestState.isChangingPassword) return

        state.update { latestState ->
            latestState.copy(
                isChangingPassword = true,
                isPasswordChangeSuccessful = false,
            )
        }
        viewModelScope.launch {
            val currentPassword = state.value.currentPasswordTextState.text.toString()
            val newPassword = state.value.newPasswordTextState.text.toString()
            authRepository
                .changePassword(
                    currentPassword = currentPassword,
                    newPassword = newPassword,
                )
                .onSuccess {
                    state.value.currentPasswordTextState.clearText()
                    state.value.newPasswordTextState.clearText()

                    state.update {
                        it.copy(
                            isChangingPassword = false,
                            newPasswordError = null,
                            isNewPasswordVisible = false,
                            isCurrentPasswordVisible = false,
                            isPasswordChangeSuccessful = true,
                        )
                    }
                }
                .onFailure { error ->
                    val errorMessage = when (error) {
                        DataError.Remote.Unauthorized -> Res.string.error_current_password_incorrect.asUiText()
                        DataError.Remote.Conflict -> Res.string.error_current_password_equal_to_new_one.asUiText()
                        else -> error.asUiText()
                    }

                    state.update { latestState ->
                        latestState.copy(
                            newPasswordError = errorMessage,
                            isChangingPassword = false,
                        )
                    }
                }
        }
    }

    private fun uploadProfilePicture(bytes: ByteArray, mimeType: String?) {
        if (state.value.isUploadingImage) return

        if (mimeType == null) {
            state.update { latestState ->
                latestState.copy(
                    imageError = Res.string.error_invalid_file_type.asUiText(),
                )
            }
            return
        }

        state.update { latestState ->
            latestState.copy(
                isUploadingImage = true,
                imageError = null,
            )
        }

        viewModelScope.launch {
            chatParticipantRepository.uploadProfilePicture(
                imageBytes = bytes,
                mimeType = mimeType,
            ).onSuccess {
                state.update {
                    it.copy(
                        isUploadingImage = false,
                    )
                }
            }.onFailure { error ->
                state.update {
                    it.copy(
                        imageError = error.asUiText(),
                        isUploadingImage = false,
                    )
                }
            }
        }
    }

    private fun fetchLocalParticipantDetails() {
        viewModelScope.launch {
            chatParticipantRepository.fetchLocalParticipant()
        }
    }

    private fun showDeleteConfirmation() {
        state.update { latestState ->
            latestState.copy(
                showDeleteConfirmationDialog = true,
            )
        }
    }

    private fun deleteProfilePicture() {
        if (state.value.isDeletingImage && state.value.profilePictureUrl == null) return

        state.update { latestState ->
            latestState.copy(
                isDeletingImage = true,
                imageError = null,
                showDeleteConfirmationDialog = false,
            )
        }

        viewModelScope.launch {
            chatParticipantRepository
                .deleteProfilePicture()
                .onSuccess {
                    state.update { latestState ->
                        latestState.copy(
                            isDeletingImage = false,
                        )
                    }
                }
                .onFailure { error ->
                    state.update { latestState ->
                        latestState.copy(
                            imageError = error.asUiText(),
                            isDeletingImage = false,
                        )
                    }
                }
        }
    }

    private fun dismissDeleteConfirmation() {
        state.update { latestState ->
            latestState.copy(
                showDeleteConfirmationDialog = false,
            )
        }
    }
}
