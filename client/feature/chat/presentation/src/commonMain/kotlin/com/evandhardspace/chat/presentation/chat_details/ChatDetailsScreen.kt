package com.evandhardspace.chat.presentation.chat_details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import chatapp.client.feature.chat.presentation.generated.resources.Res
import chatapp.client.feature.chat.presentation.generated.resources.chat_not_selected
import chatapp.client.feature.chat.presentation.generated.resources.chat_not_selected_subtitle
import chatapp.client.feature.chat.presentation.generated.resources.no_messages
import chatapp.client.feature.chat.presentation.generated.resources.no_messages_subtitle
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.DeliveryStatus
import com.evandhardspace.chat.presentation.component.ChatHeaderContent
import com.evandhardspace.chat.presentation.component.MessageBannerListener
import com.evandhardspace.chat.presentation.component.PaginationScrollListener
import com.evandhardspace.chat.presentation.component.chat_details.ChatDetailHeader
import com.evandhardspace.chat.presentation.component.chat_details.DateChip
import com.evandhardspace.chat.presentation.component.chat_details.EmptyContentSection
import com.evandhardspace.chat.presentation.component.chat_details.MessageBox
import com.evandhardspace.chat.presentation.component.chat_details.MessageList
import com.evandhardspace.chat.presentation.model.ChatUi
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.designsystem.annotations.ThemedPreview
import com.evandhardspace.core.designsystem.component.avatar.ChatParticipantUi
import com.evandhardspace.core.designsystem.component.snackbar.LocalSnackbarHostState
import com.evandhardspace.core.designsystem.theme.ChatAppPreview
import com.evandhardspace.core.designsystem.theme.extended
import com.evandhardspace.core.designsystem.theme.paddings
import com.evandhardspace.core.presentation.util.OnEffect
import com.evandhardspace.core.presentation.util.asUiText
import com.evandhardspace.core.presentation.util.compose.clearFocusOnTap
import com.evandhardspace.core.presentation.util.currentDeviceConfiguration
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import kotlin.time.Clock
import kotlin.uuid.Uuid

@Composable
internal fun ChatDetailsScreen(
    chatId: String?,
    isDetailPresent: Boolean,
    modifier: Modifier = Modifier,
    viewModel: ChatDetailsViewModel,
    onBack: () -> Unit,
    onManageChat: () -> Unit,
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarState = LocalSnackbarHostState.current
    val messageListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    LaunchedEffect(chatId) {
        viewModel.onAction(
            ChatDetailsAction.SelectChat(chatId),
        )
    }

    LaunchedEffect(chatId) {
        if (chatId != null) {
            messageListState.scrollToItem(0)
        }
    }

    OnEffect(viewModel.effects) { effect ->
        when (effect) {
            is ChatDetailsEffect.ChatLeft -> onBack()
            is ChatDetailsEffect.Error -> snackbarState.show(
                effect.error.asString(),
            )

            ChatDetailsEffect.NewMessage -> {
                scope.launch {
                    messageListState.animateScrollToItem(0)
                }
            }
        }
    }

    ChatDetailContent(
        state = state,
        isDetailPresent = isDetailPresent,
        action = viewModel::onAction,
        onBack = onBack,
        onManageChat = onManageChat,
        modifier = modifier,
        messageListState = messageListState,
    )
}

@Composable
private fun ChatDetailContent(
    state: ChatDetailsState,
    isDetailPresent: Boolean,
    action: (ChatDetailsAction) -> Unit,
    onBack: () -> Unit,
    onManageChat: () -> Unit,
    modifier: Modifier = Modifier,
    messageListState: LazyListState,
) {
    val configuration = currentDeviceConfiguration()
    val realMessageItemCount = remember(state.messages) {
        state
            .messages
            .filter { it is MessageUi.LocalUserMessage || it is MessageUi.OtherUserMessage }
            .size
    }

    MessageBannerListener(
        lazyListState = messageListState,
        messages = state.messages,
        isBannerVisible = state.bannerState.isVisible,
        onShowBanner = { index ->
            action(ChatDetailsAction.TopVisibleIndexChanged(index))
        },
        onHide = {
            action(ChatDetailsAction.HideBanner)
        }
    )

    PaginationScrollListener(
        lazyListState = messageListState,
        itemCount = realMessageItemCount,
        isPaginationLoading = state.isPaginationLoading,
        isEndReached = state.endReached,
        onNearTop = { action(ChatDetailsAction.ScrollToTop) },
    )

    LaunchedEffect(messageListState) {
        snapshotFlow {
            messageListState.firstVisibleItemIndex to messageListState.layoutInfo.totalItemsCount
        }.filter { (firstVisibleIndex, totalItemsCount) ->
            firstVisibleIndex >= 0 && totalItemsCount > 0
        }.collect { (firstVisibleItemIndex, _) ->
            action(ChatDetailsAction.FirstVisibleIndexChanged(firstVisibleItemIndex))
        }
    }

    var headerHeight by remember {
        mutableStateOf(0.dp)
    }
    val density = LocalDensity.current

    Scaffold(
        modifier = modifier
            .fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        containerColor = if (configuration.isWideScreen.not()) MaterialTheme.colorScheme.surface
        else MaterialTheme.colorScheme.extended.surfaceLower,
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .clearFocusOnTap()
                .padding(innerPadding)
                .run {
                    if (configuration.isWideScreen) padding(horizontal = MaterialTheme.paddings.half)
                    else this
                },
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                DynamicRoundedCornerColumn(
                    isCornersRounded = configuration.isWideScreen,
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                ) {
                    ChatHeaderContent(
                        modifier = Modifier.onSizeChanged {
                            headerHeight = with(density) {
                                it.height.toDp()
                            }
                        },
                    ) {
                        ChatDetailHeader(
                            chatUi = state.chatUi,
                            isDetailPresent = isDetailPresent,
                            isChatOptionsDropDownOpen = state.isChatOptionsOpen,
                            onChatOptionsClick = { action(ChatDetailsAction.ShowChatOptions) },
                            onDismissChatOptions = { action(ChatDetailsAction.DismissChatOptions) },
                            onManageChatClick = { onManageChat() },
                            onLeaveChatClick = { action(ChatDetailsAction.LeaveChat) },
                            onBackClick = onBack,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    MessageList(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        messages = state.messages,
                        messageWithOpenMenu = state.messageWithOpenMenu,
                        listState = messageListState,
                        isPaginationLoading = state.isPaginationLoading,
                        paginationError = state.paginationError?.asComposableString(),
                        onRetryPagination = { action(ChatDetailsAction.RetryPagination) },
                        onMessageLongClick = { message ->
                            action(ChatDetailsAction.MessageLongClick(message))
                        },
                        onMessageRetryClick = { message ->
                            action(ChatDetailsAction.RetrySendMessage(message))
                        },
                        onDismissMessageMenu = {
                            action(ChatDetailsAction.DismissMessageMenu)
                        },
                        onDeleteMessageClick = { message ->
                            action(ChatDetailsAction.DeleteMessage(message))
                        },
                        emptyContent = {
                            EmptyContentSection(
                                title = stringResource(
                                    if (state.chatUi != null) Res.string.no_messages
                                    else Res.string.chat_not_selected,
                                ),
                                description = stringResource(
                                    if (state.chatUi != null) Res.string.no_messages_subtitle
                                    else Res.string.chat_not_selected_subtitle,
                                ),
                            )
                        }
                    )

                    AnimatedVisibility(
                        visible = configuration.isWideScreen.not() && state.chatUi != null,
                    ) {
                        MessageBox(
                            messageTextFieldState = state.messageTextFieldState,
                            isSendEnabled = state.canSendMessage,
                            connectionState = state.connectionState,
                            onSendClick = { action(ChatDetailsAction.SendMessage) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.paddings.half),
                        )
                    }
                }

                if (configuration.isWideScreen) {
                    Spacer(modifier = Modifier.height(MaterialTheme.paddings.half))
                }

                AnimatedVisibility(
                    visible = configuration.isWideScreen && state.chatUi != null,
                ) {
                    DynamicRoundedCornerColumn(
                        isCornersRounded = configuration.isWideScreen
                    ) {
                        MessageBox(
                            messageTextFieldState = state.messageTextFieldState,
                            isSendEnabled = state.canSendMessage,
                            connectionState = state.connectionState,
                            onSendClick = { action(ChatDetailsAction.SendMessage) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(MaterialTheme.paddings.half),
                        )
                    }
                }
            }

            AnimatedVisibility(
                visible = state.bannerState.isVisible,
                modifier =
                    Modifier.align(Alignment.TopCenter)
                        .padding(top = headerHeight + MaterialTheme.paddings.default),
                enter = fadeIn(),
                exit = fadeOut(),
            ) {
                if (state.bannerState.formattedDate != null) {
                    DateChip(
                        date = state.bannerState.formattedDate.asComposableString(),
                    )
                }
            }
        }
    }
}

@Composable
private fun DynamicRoundedCornerColumn(
    isCornersRounded: Boolean,
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(
        modifier = modifier
            .shadow(
                elevation = if (isCornersRounded) 8.dp else 0.dp,
                shape = if (isCornersRounded) RoundedCornerShape(24.dp) else RectangleShape,
                spotColor = Color.Black.copy(alpha = 0.2f),
            )
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = if (isCornersRounded) RoundedCornerShape(24.dp) else RectangleShape,
            )
    ) { content() }
}

@ThemedPreview
@Composable
private fun ChatDetailEmptyPreview() {
    ChatAppPreview {
        ChatDetailContent(
            state = ChatDetailsState(),
            isDetailPresent = false,
            action = {},
            onBack = {},
            onManageChat = {},
            messageListState = rememberLazyListState(),
        )
    }
}

@ThemedPreview
@Composable
private fun ChatDetailMessagesPreview() {
    ChatAppPreview {
        ChatDetailContent(
            state = ChatDetailsState(
                messageTextFieldState = rememberTextFieldState(
                    initialText = "This is a new message!"
                ),
                canSendMessage = true,
                chatUi = ChatUi(
                    id = "1",
                    localParticipant = ChatParticipantUi(
                        id = "1",
                        username = "Ivan",
                        initials = "IV",
                    ),
                    otherParticipants = listOf(
                        ChatParticipantUi(
                            id = "2",
                            username = "John",
                            initials = "JO",
                        ),
                        ChatParticipantUi(
                            id = "3",
                            username = "Dan",
                            initials = "DA",
                        ),
                    ),
                    latestMessage = ChatMessage(
                        id = "1",
                        chatId = "1",
                        content = "This is a last chat message that was sent by Philipp " +
                                "and goes over multiple lines to showcase the ellipsis",
                        createdAt = Clock.System.now(),
                        senderId = "1",
                        deliveryStatus = DeliveryStatus.Sent,
                    ),
                    latestMessageSenderUsername = "IV",
                ),
                messages = (1..20).map {
                    if (it % 2 == 0) {
                        MessageUi.LocalUserMessage(
                            id = it.toString(),
                            content = "Hello world!",
                            deliveryStatus = DeliveryStatus.Sent,
                            formattedSentTime = "Friday, Aug 20".asUiText(),
                        )
                    } else {
                        MessageUi.OtherUserMessage(
                            id = it.toString(),
                            content = "Hello world!",
                            sender = ChatParticipantUi(
                                id = Uuid.random().toString(),
                                username = "John",
                                initials = "JO",
                            ),
                            formattedSentTime = "Friday, Aug 20".asUiText(),
                        )
                    }
                }
            ),
            isDetailPresent = false,
            action = {},
            onBack = {},
            onManageChat = {},
            messageListState = rememberLazyListState(),
        )
    }
}
