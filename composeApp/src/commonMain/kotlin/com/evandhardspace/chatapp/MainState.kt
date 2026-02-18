package com.evandhardspace.chatapp

sealed interface MainState {
    data object Loading: MainState
    data class Loaded(val isLoggedIn: Boolean): MainState
}
