package com.evandhardspace.chatapp

sealed interface MainState {
    data object Loading: MainState
    data class Loaded(val isAuthorized: Boolean): MainState
}
