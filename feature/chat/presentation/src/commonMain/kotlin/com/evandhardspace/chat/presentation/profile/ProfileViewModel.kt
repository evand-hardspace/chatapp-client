package com.evandhardspace.chat.presentation.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel

@KoinViewModel
internal class ProfileViewModel : ViewModel() {

    private val _effects = Channel<ProfileEffect>()
    val effects = _effects.receiveAsFlow()

    val state: StateFlow<ProfileState>
        field = MutableStateFlow(ProfileState())

    fun onAction(action: ProfileAction) {
        when (action) {
            is ProfileAction.OnChangePasswordClick -> Unit
            is ProfileAction.OnConfirmDeleteClick -> Unit
            is ProfileAction.OnDeletePictureClick -> Unit
            is ProfileAction.OnDismiss -> {
                viewModelScope.launch { _effects.send(ProfileEffect.Dismiss) }
            }

            is ProfileAction.OnDismissDeleteConfirmationDialogClick -> Unit
            is ProfileAction.OnErrorImagePicker -> Unit
            is ProfileAction.OnPictureSelected -> Unit
            is ProfileAction.OnToggleCurrentPasswordVisibility -> Unit
            is ProfileAction.OnToggleNewPasswordVisibility -> Unit
            is ProfileAction.OnUploadPictureClick -> Unit
            is ProfileAction.OnUriSelected -> Unit
        }
    }
}
