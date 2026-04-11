package com.evandhardspace.chat.presentation.component.manage_chat

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.ime
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import chatapp.client.feature.chat.presentation.generated.resources.Res
import chatapp.client.feature.chat.presentation.generated.resources.cancel
import chatapp.client.feature.chat.presentation.generated.resources.remove_participant
import com.evandhardspace.chat.presentation.component.ChatParticipantSearchTextSection
import com.evandhardspace.chat.presentation.component.ChatParticipantsSelectionSection
import com.evandhardspace.chat.presentation.component.ManageChatButtonSection
import com.evandhardspace.chat.presentation.component.ManageChatHeaderRow
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.designsystem.component.brand.ChatAppHorizontalDivider
import com.evandhardspace.core.designsystem.component.button.ChatAppButton
import com.evandhardspace.core.designsystem.component.button.ChatAppButtonStyle
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.iconSize
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.DeviceConfiguration
import com.evandhardspace.core.presentation.util.compose.clearFocusOnTap
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.stringResource

@Composable
internal fun ManageChatContent(
    primaryButtonText: String,
    headerText: String,
    state: ManageChatState,
    action: (ManageChatAction) -> Unit,
    onDismiss: () -> Unit,
) {
    var isTextFieldFocused by remember { mutableStateOf(false) }
    val imeHeight = WindowInsets.ime.getBottom(LocalDensity.current)
    val isKeyboardVisible = imeHeight > 0
    val configuration = currentDeviceConfiguration()

    val shouldHideHeader = configuration == DeviceConfiguration.MobileLandscape
            && (isKeyboardVisible || isTextFieldFocused)

    Column(
        modifier = Modifier
            .clearFocusOnTap()
            .fillMaxWidth()
            .wrapContentHeight()
            .imePadding()
            .background(MaterialTheme.colorScheme.surface)
            .navigationBarsPadding(),
    ) {
        AnimatedVisibility(
            visible = shouldHideHeader.not(),
        ) {
            Column {
                ManageChatHeaderRow(
                    title = headerText,
                    onCloseClick = onDismiss,
                    modifier = Modifier.fillMaxWidth(),
                )
                ChatAppHorizontalDivider()
            }
        }
        ChatParticipantSearchTextSection(
            queryState = state.queryTextState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.paddings.default)
                .padding(top = MaterialTheme.paddings.default),
            error = state.searchError,
            onFocusChanged = { isTextFieldFocused = it },
        )

        AnimatedVisibility(state.selectedChatParticipants.isNotEmpty()) {
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.paddings.quarter),
                contentPadding = PaddingValues(
                    vertical = MaterialTheme.paddings.half,
                    horizontal = MaterialTheme.paddings.default,
                )
            ) {
                items(
                    items = state.selectedChatParticipants,
                    key = { it.id },
                ) { participant ->
                    ChatParticipantChip(
                        participant = participant,
                        onRemoveClick = { action(ManageChatAction.RemoveSelectedParticipant(it)) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                    )
                }
            }
        }

        ChatParticipantsSelectionSection(
            existingParticipants = state.existingChatParticipants,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = MaterialTheme.paddings.default),
            searchResult = state.currentSearchResult,
            onSearchParticipant = { action(ManageChatAction.SelectParticipant(it)) },
            isLoading = state.isSearching,
            localParticipant = state.localUser,
        )

        ChatAppHorizontalDivider()
        ManageChatButtonSection(
            primaryButton = {
                ChatAppButton(
                    text = primaryButtonText,
                    onClick = {
                        action(ManageChatAction.Submit)
                    },
                    enabled = state.selectedChatParticipants.isNotEmpty(),
                    isLoading = state.isSubmitting,
                )
            },
            secondaryButton = {
                ChatAppButton(
                    text = stringResource(Res.string.cancel),
                    onClick = onDismiss,
                    style = ChatAppButtonStyle.Secondary,
                )
            },
            error = state.submitError?.asComposableString(),
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

@Composable
private fun ChatParticipantChip(
    participant: ChatParticipantUi,
    onRemoveClick: (ChatParticipantUi) -> Unit,
    modifier: Modifier = Modifier,
) {
    var isRemovable by remember { mutableStateOf(false) }
    val containerColor by animateColorAsState(
        if (isRemovable) MaterialTheme.colorScheme.errorContainer
        else MaterialTheme.colorScheme.primary,
    )

    LaunchedEffect(isRemovable) {
        delay(RemoveSelectedParticipantDelayMs)
        isRemovable = false
    }

    Row(
        modifier = modifier
            .clip(RoundedCornerShape(percent = 100))
            .clickable {
                if (isRemovable) {
                    onRemoveClick(participant)
                } else {
                    isRemovable = true
                }
            }
            .background(containerColor)
            .border(
                width = 2.dp,
                color = MaterialTheme.colorScheme.outline,
                shape = RoundedCornerShape(percent = 100),
            )
            .padding(
                vertical = 6.dp,
                horizontal = 12.dp,
            ),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        val contentColor by animateColorAsState(
            if (isRemovable) MaterialTheme.colorScheme.onError
            else MaterialTheme.colorScheme.onPrimary
        )
        Text(
            text = participant.username,
            modifier = Modifier,
            style = MaterialTheme.typography.labelLarge,
            color = contentColor,
        )
        AnimatedVisibility(isRemovable) {
            Icon(
                modifier = Modifier.size(MaterialTheme.iconSize.medium),
                imageVector = Icons.Default.Close,
                contentDescription = stringResource(Res.string.remove_participant),
                tint = contentColor,
            )
        }
    }
}

private const val RemoveSelectedParticipantDelayMs = 2000L

@ThemedPreview
@Composable
private fun ManageChatScreenPreview() {
    ChatAppPreview {
        val existingParticipants = listOf(
            ChatParticipantUi("1", "Ivan", "IV", null),
            ChatParticipantUi("2", "John", "JO", null)
        )
        val selectedParticipants = listOf(
            ChatParticipantUi("3", "Dan", "DA", null),
            ChatParticipantUi("4", "Nathan", "NA", null),
        )
        val searchParticipant = ChatParticipantUi("4", "Bob", "BO", null)
        ManageChatContent(
            state = ManageChatState(
                existingChatParticipants = existingParticipants,
                selectedChatParticipants = selectedParticipants,
                currentSearchResult = CurrentSearchResultState(searchParticipant, CurrentSearchResultState.Status.New),
            ),
            action = {},
            onDismiss = {},
            primaryButtonText = "Create",
            headerText = "Create chat",
        )
    }
}
