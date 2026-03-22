package com.evandhardspace.chat.presentation.component

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import chatapp.feature.chat.presentation.generated.resources.Res
import chatapp.feature.chat.presentation.generated.resources.add
import chatapp.feature.chat.presentation.generated.resources.participants
import chatapp.feature.chat.presentation.generated.resources.search_result
import chatapp.feature.chat.presentation.generated.resources.you
import com.evandhardspace.chat.presentation.component.manage_chat.CurrentSearchResultState
import com.evandhardspace.chat.presentation.component.manage_chat.CurrentSearchResultState.Status
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.ChatAppLoadingIndicator
import com.evandhardspace.core.designsystem.component.avatar.ChatAppAvatarPhoto
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.designsystem.component.brand.ChatAppHorizontalDivider
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.designsystem.theme.titleExtraSmall
import com.evandhardspace.core.presentation.util.DeviceConfiguration
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ColumnScope.ChatParticipantsSelectionSection(
    existingParticipants: List<ChatParticipantUi>,
    localParticipant: ChatParticipantUi?,
    isLoading: Boolean,
    searchResult: CurrentSearchResultState,
    onSearchParticipant: (ChatParticipantUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    val deviceConfiguration = currentDeviceConfiguration()
    val rootHeightModifier = when (deviceConfiguration) {
        DeviceConfiguration.TabletPortrait,
        DeviceConfiguration.TabletLandscape,
        DeviceConfiguration.Desktop -> {
            Modifier
                .animateContentSize()
                .heightIn(min = 200.dp, max = 300.dp)
        }

        else -> Modifier.weight(1f)
    }

    Box(
        modifier = rootHeightModifier then modifier.background(MaterialTheme.colorScheme.surface),
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.default),
            contentPadding = PaddingValues(top = MaterialTheme.paddings.half),
        ) {
            searchResult.participant.takeIf { searchResult.status == Status.New }
                ?.let { searchParticipant ->
                    stickyHeader {
                        Column(Modifier.animateItem()) {
                            Text(
                                text = stringResource(Res.string.search_result),
                                style = MaterialTheme.typography.titleSmall,
                                color = MaterialTheme.colorScheme.extended.textSecondary,
                            )
                            ChatParticipantListItem(
                                participant = searchParticipant,
                                onSearchParticipant = onSearchParticipant,
                                isLoading = isLoading,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = MaterialTheme.paddings.half),
                            )
                            if (existingParticipants.isNotEmpty()) {
                                ChatAppHorizontalDivider()
                            }
                        }
                    }
                }

            if (existingParticipants.isNotEmpty() || localParticipant != null) {
                item {
                    Text(
                        modifier = Modifier.animateItem(),
                        text = stringResource(Res.string.participants),
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.extended.textSecondary,
                    )
                }
            }

            if (localParticipant != null) {
                item {
                    ChatParticipantListItem(
                        modifier = Modifier.animateItem(),
                        participant = localParticipant,
                        isLocal = true,
                    )
                }
            }

            if (existingParticipants.isNotEmpty()) {
                items(
                    items = existingParticipants,
                    key = { it.id },
                ) { participant ->
                    ChatParticipantListItem(
                        participant = participant,
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                    )
                }
            }
        }
    }
}

@Composable
private fun ChatParticipantListItem(
    participant: ChatParticipantUi,
    isLocal: Boolean = false,
    onSearchParticipant: ((ChatParticipantUi) -> Unit)? = null,
    isLoading: Boolean = false,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.threeQuarters),
    ) {
        ChatAppAvatarPhoto(
            displayText = participant.initials,
            imageUrl = participant.imageUrl,
        )
        Text(
            text = participant.username,
            style = MaterialTheme.typography.titleExtraSmall,
            color = MaterialTheme.colorScheme.extended.textPrimary,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )

        if (isLocal) {
            Text(
                text = "(${stringResource(Res.string.you)})",
                style = MaterialTheme.typography.titleExtraSmall,
                color = MaterialTheme.colorScheme.extended.textSecondary,
            )
        }

        if (isLoading) {
            Spacer(Modifier.weight(1f))
            ChatAppLoadingIndicator()
        } else {
            onSearchParticipant?.let {
                Spacer(Modifier.weight(1f))
                ChatAppButton(
                    text = stringResource(Res.string.add),
                    onClick = { onSearchParticipant(participant) },
                    style = ChatAppButtonStyle.Primary,
                )
            }
        }
    }
}

@ThemedPreview
@Composable
private fun ChatParticipantSelectionSectionPreview() {
    ChatAppPreview {
        val existingParticipants = listOf(
            ChatParticipantUi("1", "Ivan", "IV", null),
            ChatParticipantUi("2", "John", "JO", null)
        )
        val searchParticipant = ChatParticipantUi("4", "Bob", "BO", null)
        Column {
            ChatParticipantsSelectionSection(
                existingParticipants = existingParticipants,
                searchResult = CurrentSearchResultState(searchParticipant, Status.New),
                onSearchParticipant = {},
                isLoading = false,
                localParticipant = null,
            )
        }
    }
}
