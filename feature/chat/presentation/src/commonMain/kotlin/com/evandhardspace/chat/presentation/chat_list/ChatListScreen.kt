package com.evandhardspace.chat.presentation.chat_list

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview

@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    Column(modifier) {
        Text("Chat List")
    }
}

@ThemedPreview
@Composable
fun ChatListScreenPreview() {
    ChatAppPreview {
        ChatListScreen()
    }
}