package com.evandhardspace.chat.presentation.navigation

import kotlinx.serialization.Serializable

sealed interface ChatNavGraphRoute {

    @Serializable
    data object Root : ChatNavGraphRoute

    @Serializable
    data object ChatListRoute : ChatNavGraphRoute
}