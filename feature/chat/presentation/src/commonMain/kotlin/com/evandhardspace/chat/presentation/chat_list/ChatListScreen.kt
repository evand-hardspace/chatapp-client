package com.evandhardspace.chat.presentation.chat_list

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview

@Composable
fun ChatListScreen(
    modifier: Modifier = Modifier.fillMaxSize(),
) {
    Box(
        modifier,
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = "Chat List",
            color = MaterialTheme.colorScheme.primary,
        )
    }
}

@ThemedPreview
@Composable
fun ChatListScreenPreview() {
    ChatAppPreview {
        ChatListScreen()
    }
}
