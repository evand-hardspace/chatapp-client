package com.evandhardspace.chat.presentation.navigation

import com.evandhardspace.core.presentation.navigation.NavRoute
import kotlinx.serialization.Serializable

sealed interface ChatNavGraphRoute: NavRoute {

    @Serializable
    data object Root : ChatNavGraphRoute

    @Serializable
    data object ChatListRoute : ChatNavGraphRoute
}