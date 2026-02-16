package com.evandhardspace.auth.presentation.register

internal sealed interface RegisterAction {
    data object OnInputTextFocusGain: RegisterAction
    data object OnRegister: RegisterAction
    data object OnTogglePasswordVisibility: RegisterAction
}
