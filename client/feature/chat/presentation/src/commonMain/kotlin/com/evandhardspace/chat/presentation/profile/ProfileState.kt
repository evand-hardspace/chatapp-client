package com.evandhardspace.chat.presentation.profile

import androidx.compose.foundation.text.input.TextFieldState
import com.evandhardspace.core.presentation.util.UiText

internal data class ProfileState(
    val username: String = "",
    val userInitials: String = "--",
    val profilePictureUrl: String? = null,
    val isUploadingImage: Boolean = false,
    val isDeletingImage: Boolean = false,
    val showDeleteConfirmationDialog: Boolean = false,
    val imageError: UiText? = null,
    val emailTextState: TextFieldState = TextFieldState(),
    val currentPasswordTextState: TextFieldState = TextFieldState(),
    val newPasswordTextState: TextFieldState = TextFieldState(),
    val isCurrentPasswordVisible: Boolean = false,
    val isNewPasswordVisible: Boolean = false,
    val isChangingPassword: Boolean = false,
    val currentPasswordError: UiText? = null,
    val newPasswordError: UiText? = null,
    val canChangePassword: Boolean = false,
    val isPasswordChangeSuccessful: Boolean = false,
)
