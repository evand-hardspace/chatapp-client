package com.evandhardspace.chatapp.deeplink.fallback

internal sealed interface DeeplinkFallbackState {
    data object Loading : DeeplinkFallbackState
    data class Loaded(val isAuthorized: Boolean) : DeeplinkFallbackState
}
