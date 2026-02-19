package com.evandhardspace.chatapp

sealed interface MainEffect {
    data object LoggedOut: MainEffect
}
