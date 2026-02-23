package com.evandhardspace.core.designsystem.component.avatar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.theme.ChatAppPreview

@Composable
fun ChatAppStackedAvatars(
    avatars: List<AvatarUi>,
    modifier: Modifier = Modifier,
    size: AvatarSize = AvatarSize.Small,
    maxVisible: Int = 2,
    overlapPercentage: Float = 0.4f,
) {
    val overlapOffset = -(size.dp * overlapPercentage)

    val visibleAvatars = avatars.take(maxVisible)
    val remainingCount = (avatars.size - maxVisible).coerceAtLeast(0)

    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(overlapOffset),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        visibleAvatars.forEach { avatarUi ->
            ChatAppAvatarPhoto(
                displayText = avatarUi.initials,
                size = size,
                imageUrl = avatarUi.imageUrl,
            )
        }

        if(remainingCount > 0) {
            ChatAppAvatarPhoto(
                displayText = "$remainingCount+",
                size = size,
                textColor = MaterialTheme.colorScheme.primary,
            )
        }
    }
}

@ThemedPreview
@Composable
fun ChatAppStackedAvatarsPreview() {
    ChatAppPreview {
        ChatAppStackedAvatars(
            avatars = listOf(
                AvatarUi(
                    id = "1",
                    username = "Ivan",
                    initials = "IK",
                ),
                AvatarUi(
                    id = "2",
                    username = "John",
                    initials = "JS",
                ),
                AvatarUi(
                    id = "3",
                    username = "Bob",
                    initials = "BM",
                ),
                AvatarUi(
                    id = "4",
                    username = "Charlie",
                    initials = "CF",
                ),
            )
        )
    }
}
