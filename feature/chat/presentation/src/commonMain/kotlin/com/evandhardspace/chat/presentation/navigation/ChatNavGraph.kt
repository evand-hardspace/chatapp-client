package com.evandhardspace.chat.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.evandhardspace.chat.presentation.chat_list_details.ChatListDetailsScene

fun NavGraphBuilder.chatNavGraph(
    navController: NavController,
) {
    navigation<ChatNavGraphRoute.Root>(
        startDestination = ChatNavGraphRoute.ChatListDetailsRoute,
    ) {
        composable<ChatNavGraphRoute.ChatListDetailsRoute> {
            ChatListDetailsScene(
                onLogout = {
                    // TODO: Logout user
                }
            )
        }
    }
}
