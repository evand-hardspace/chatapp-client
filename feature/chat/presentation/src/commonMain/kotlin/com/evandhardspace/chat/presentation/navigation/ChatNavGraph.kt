package com.evandhardspace.chat.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.evandhardspace.chat.presentation.chat_list.ChatListScreen

fun NavGraphBuilder.chatNavGraph() {
    navigation<ChatNavGraphRoute.Root>(
        startDestination = ChatNavGraphRoute.ChatListRoute,
    ) {
        composable<ChatNavGraphRoute.ChatListRoute> {
            ChatListScreen()
        }
    }
}
