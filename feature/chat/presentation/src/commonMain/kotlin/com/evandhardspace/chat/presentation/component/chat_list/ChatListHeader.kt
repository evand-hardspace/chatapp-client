package com.evandhardspace.chat.presentation.component.chat_list

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.hideFromAccessibility
import androidx.compose.ui.semantics.semantics
import chatapp.core.design_system.generated.resources.logo_chatapp
import chatapp.core.design_system.generated.resources.users_icon
import chatapp.core.design_system.generated.resources.logout_icon
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.app_name
import chatapp.feature.chat.presentation.generated.resources.logout
import chatapp.feature.chat.presentation.generated.resources.profile_settings
import com.evandhardspace.chat.presentation.component.ChatHeaderContent
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.avatar.ChatAppAvatarPhoto
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.designsystem.component.dropdown.ChatAppDropdownMenu
import com.evandhardspace.core.designsystem.component.dropdown.DropdownItem
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import chatapp.core.design_system.generated.resources.Res as DesignSystemRes

@Composable
internal fun ChatListHeader(
    localParticipant: ChatParticipantUi?,
    isUserMenuOpen: Boolean,
    onUserAvatarClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    ChatHeaderContent(
        modifier = modifier
            .fillMaxWidth(),
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.default),
            modifier = Modifier.fillMaxWidth()
        ) {
            Image(
                modifier = Modifier.semantics { hideFromAccessibility() },
                imageVector = vectorResource(DesignSystemRes.drawable.logo_chatapp),
                contentDescription = null,
            )
            Text(
                text = stringResource(Res.string.app_name),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.extended.textPrimary,
            )
            Spacer(modifier = Modifier.weight(1f))
            ProfileAvatarSection(
                localParticipant = localParticipant,
                isMenuExpanded = isUserMenuOpen,
                onClick = onUserAvatarClick,
                onDismissMenu = onDismissMenu,
                onProfileSettingsClick = onProfileSettingsClick,
                onLogoutClick = onLogoutClick,
            )
        }
    }
}

@Composable
private fun ProfileAvatarSection(
    localParticipant: ChatParticipantUi?,
    isMenuExpanded: Boolean,
    onClick: () -> Unit,
    onDismissMenu: () -> Unit,
    onProfileSettingsClick: () -> Unit,
    onLogoutClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val onLogoutClick = {
        onDismissMenu()
        onLogoutClick()
    }
    val onProfileSettingsClick = {
        onDismissMenu()
        onProfileSettingsClick()
    }

    Box(
        modifier = modifier
            .padding(bottom = MaterialTheme.paddings.quarter),
    ) {
        if (localParticipant != null) {
            ChatAppAvatarPhoto(
                displayText = localParticipant.initials,
                imageUrl = localParticipant.imageUrl,
                onClick = onClick,
            )
        }

        ChatAppDropdownMenu(
            expanded = isMenuExpanded,
            onDismiss = onDismissMenu,
            items = listOf(
                DropdownItem(
                    title = stringResource(Res.string.profile_settings),
                    icon = vectorResource(DesignSystemRes.drawable.users_icon),
                    contentColor = MaterialTheme.colorScheme.extended.textSecondary,
                    onClick = onProfileSettingsClick,
                ),
                DropdownItem(
                    title = stringResource(Res.string.logout),
                    icon = vectorResource(DesignSystemRes.drawable.logout_icon),
                    contentColor = MaterialTheme.colorScheme.extended.destructiveHover,
                    onClick = onLogoutClick,
                ),
            )
        )
    }
}

@ThemedPreview
@Composable
private fun ChatListHeaderPreview() {
    ChatAppPreview {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface),
        ) {
            ChatListHeader(
                localParticipant = ChatParticipantUi(
                    id = "1",
                    username = "Ivan",
                    initials = "IV",
                ),
                isUserMenuOpen = true,
                onUserAvatarClick = {},
                onDismissMenu = {},
                onProfileSettingsClick = {},
                onLogoutClick = {},
            )
        }
    }
}