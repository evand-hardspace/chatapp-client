package com.evandhardspace.chat.presentation.profile

internal sealed interface ProfileEffect {
    data object Dismiss: ProfileEffect
}