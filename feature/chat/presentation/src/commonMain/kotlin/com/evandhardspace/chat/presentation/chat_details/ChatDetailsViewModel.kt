package com.evandhardspace.chat.presentation.chat_details

import androidx.compose.foundation.text.input.clearText
import androidx.compose.runtime.snapshotFlow
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.evandhardspace.chat.domain.model.ChatMessage
import com.evandhardspace.chat.domain.model.ConnectionState
import com.evandhardspace.chat.domain.model.OutgoingNewMessage
import com.evandhardspace.chat.domain.repository.ChatConnectionRepository
import com.evandhardspace.chat.domain.repository.ChatRepository
import com.evandhardspace.chat.domain.repository.MessageRepository
import com.evandhardspace.chat.presentation.mapper.toUi
import com.evandhardspace.chat.presentation.mapper.toUiList
import com.evandhardspace.chat.presentation.model.MessageUi
import com.evandhardspace.core.common.stateInUi
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.SessionRepository
import com.evandhardspace.core.domain.util.DataErrorException
import com.evandhardspace.core.domain.util.Paginator
import com.evandhardspace.core.domain.util.onFailure
import com.evandhardspace.core.domain.util.onSuccess
import com.evandhardspace.core.presentation.util.UiText
import com.evandhardspace.core.presentation.util.asUiText
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.annotation.KoinViewModel
import kotlin.uuid.Uuid

@KoinViewModel
internal class ChatDetailsViewModel(
    private val chatRepository: ChatRepository,
    private val sessionRepository: SessionRepository,
    private val messageRepository: MessageRepository,
    private val connectionRepository: ChatConnectionRepository,
) : ViewModel() {

    private val _effects = Channel<ChatDetailsEffect>()
    val effects = _effects.receiveAsFlow()

    private var currentPaginator: Paginator<String?, ChatMessage>? = null

    private val selectedChatId = MutableStateFlow<String?>(null)

    private val chatInfoFlow = selectedChatId
        .onEach { chatId ->
            if (chatId != null) {
                setupPaginatorForChat(chatId)
            } else {
                currentPaginator = null
            }
        }
        .flatMapLatest { chatId ->
            if (chatId != null) chatRepository.getChatInfoById(chatId)
            else emptyFlow()
        }

    private val _state = MutableStateFlow(ChatDetailsState())

    private val canSendMessage = snapshotFlow { _state.value.messageTextFieldState.text.toString() }
        .map(CharSequence::isBlank)
        .combine(connectionRepository.connectionState) { isMessageBlank, connectionState ->
            isMessageBlank.not() && connectionState == ConnectionState.Connected
        }

    private val stateWithMessages: Flow<ChatDetailsState> = combine(
        _state,
        chatInfoFlow,
        sessionRepository.authState,
    ) { currentState, chatInfo, authState ->
        if (authState !is AuthState.Authenticated) return@combine ChatDetailsState()

        // TODO: reduce update frequency on date banner state
        currentState.copy(
            chatUi = chatInfo.chat.toUi(authState.user.id),
            messages = chatInfo.messages.toUiList(authState.user.id),
        )
    }

    val state = selectedChatId
        .flatMapLatest { chatId ->
            if (chatId != null) stateWithMessages
            else _state
        }
        .stateInUi(ChatDetailsState())

    init {
        observeConnectionState()
        observeChatMessages()
        observeCanSendMessage()
    }

    fun onAction(action: ChatDetailsAction) {
        when (action) {
            is ChatDetailsAction.ShowChatOptions -> onChatOptions()
            is ChatDetailsAction.DeleteMessage -> onDeleteMessage(action.message)
            is ChatDetailsAction.DismissChatOptions -> onDismissChatOptions()
            is ChatDetailsAction.DismissMessageMenu -> onDismissMessageMenu()
            is ChatDetailsAction.LeaveChat -> onLeaveChat()
            is ChatDetailsAction.MessageLongClick -> onMessageLongClick(action.message)
            is ChatDetailsAction.RetrySendMessage -> onRetryMessage(action.message)
            is ChatDetailsAction.ScrollToTop -> onScrollToTop()
            is ChatDetailsAction.SelectChat -> switchChat(action.chatId)
            is ChatDetailsAction.SendMessage -> onSendMessage()
            is ChatDetailsAction.FirstVisibleIndexChanged -> updateNearBottom(action.index)
            is ChatDetailsAction.RetryPagination -> retryPagination()
            is ChatDetailsAction.HideBanner -> onHideBanner()
            is ChatDetailsAction.TopVisibleIndexChanged -> updateBanner(action.topVisibleIndex)
        }
    }

    private fun updateBanner(topVisibleIndex: Int) {
        val visibleDate = calculateBannerDateFromIndex(
            messages = state.value.messages,
            index = topVisibleIndex
        )

        _state.update {
            it.copy(
                bannerState = BannerState(
                    formattedDate = visibleDate,
                    isVisible = visibleDate != null
                )
            )
        }
    }

    private fun calculateBannerDateFromIndex(
        messages: List<MessageUi>,
        index: Int
    ): UiText? {
        if (messages.isEmpty() || index < 0 || index >= messages.size) return null

        return (index until messages.size)
            .firstNotNullOfOrNull { index ->
            val item = messages.getOrNull(index)
            if (item is MessageUi.DateSeparator) item.date else null
        }
    }

    private fun onHideBanner() {
        _state.update {
            it.copy(
                bannerState = it.bannerState.copy(
                    isVisible = false,
                )
            )
        }
    }


    private fun retryPagination() = loadNextItems()

    private fun onScrollToTop() = loadNextItems()

    private fun loadNextItems() {
        viewModelScope.launch {
            currentPaginator?.loadNextItems()
        }
    }

    private fun onDismissMessageMenu() {
        _state.update { latestState ->
            latestState.copy(
                messageWithOpenMenu = null,
            )
        }
    }

    private fun onMessageLongClick(message: MessageUi.LocalUserMessage) {
        _state.update { latestState ->
            latestState.copy(
                messageWithOpenMenu = message,
            )
        }
    }

    private fun onDeleteMessage(message: MessageUi.LocalUserMessage) {
        viewModelScope.launch {
            messageRepository
                .deleteMessage(message.id)
                .onFailure { error ->
                    _effects.send(ChatDetailsEffect.Error(error.asUiText()))
                }
        }
    }

    private fun onRetryMessage(message: MessageUi.LocalUserMessage) {
        viewModelScope.launch {
            messageRepository
                .retryMessage(message.id)
                .onFailure { error ->
                    _effects.send(ChatDetailsEffect.Error(error.asUiText()))
                }
        }
    }

    private fun updateNearBottom(firstVisibleIndex: Int) {
        _state.update {
            it.copy(
                isNearBottom = firstVisibleIndex <= 3,
            )
        }
    }

    private fun observeChatMessages() {
        val currentMessages = state
            .map { it.messages }
            .distinctUntilChanged()

        val newMessages = selectedChatId.flatMapLatest { chatId ->
            if (chatId != null) messageRepository.getMessagesForChat(chatId)
            else emptyFlow()
        }

        val isNearBottom = state.map { it.isNearBottom }.distinctUntilChanged()

        combine(
            currentMessages,
            newMessages,
            isNearBottom,
        ) { currentMessages, newMessages, isNearBottom ->
            val lastNewId = newMessages.lastOrNull()?.message?.id
            val lastCurrentId = currentMessages.lastOrNull()?.id

            if (lastNewId != lastCurrentId && isNearBottom) {
                _effects.send(ChatDetailsEffect.NewMessage)
            }
        }.launchIn(viewModelScope)
    }

    private fun observeConnectionState() {
        connectionRepository
            .connectionState
            .onEach { connectionState ->
                if (connectionState == ConnectionState.Connected) {
                    currentPaginator?.loadNextItems()
                }

                _state.update {
                    it.copy(
                        connectionState = connectionState,
                    )
                }
            }
            .launchIn(viewModelScope)
    }

    private fun observeCanSendMessage() {
        canSendMessage.onEach { canSend ->
            _state.update {
                it.copy(
                    canSendMessage = canSend,
                )
            }
        }.launchIn(viewModelScope)
    }

    private fun switchChat(chatId: String?) {
        selectedChatId.update { chatId }
        viewModelScope.launch {
            chatId?.let {
                chatRepository.fetchChatById(chatId)
            }
        }
    }

    private fun onChatOptions() {
        _state.update {
            it.copy(isChatOptionsOpen = true)
        }
    }

    private fun onDismissChatOptions() {
        _state.update {
            it.copy(isChatOptionsOpen = false)
        }
    }

    private fun onSendMessage() {
        val currentChatId = selectedChatId.value
        val content = state.value.messageTextFieldState.text.toString().trim()
        if (content.isBlank() || currentChatId == null) return

        viewModelScope.launch {
            val message = OutgoingNewMessage(
                chatId = currentChatId,
                messageId = Uuid.random().toString(),
                content = content,
            )

            messageRepository
                .sendMessage(message)
                .onSuccess {
                    state.value.messageTextFieldState.clearText()
                }
                .onFailure { error ->
                    _effects.send(ChatDetailsEffect.Error(error.asUiText()))
                }
        }
    }


    private fun onLeaveChat() {
        val chatId = selectedChatId.value ?: return

        _state.update {
            it.copy(isChatOptionsOpen = false)
        }

        viewModelScope.launch {
            chatRepository.leaveChat(chatId)
                .onSuccess {
                    _state.value.messageTextFieldState.clearText()

                    selectedChatId.update { null }

                    _state.update { latestState ->
                        latestState.copy(
                            chatUi = null,
                            messages = emptyList(),
                            bannerState = BannerState(),
                        )
                    }
                    _effects.send(
                        ChatDetailsEffect.ChatLeft,
                    )
                }
                .onFailure { error ->
                    _effects.send(
                        ChatDetailsEffect.Error(
                            error = error.asUiText(),
                        ),
                    )
                }
        }
    }

    private fun setupPaginatorForChat(chatId: String) {
        currentPaginator = Paginator(
            initialKey = null,
            onLoadUpdated = { isLoading ->
                _state.update { latestState ->
                    latestState.copy(
                        isPaginationLoading = isLoading,
                    )
                }
            },
            onRequest = { beforeTimestamp ->
                messageRepository.fetchMessages(chatId, beforeTimestamp)
            },
            getNextKey = { messages ->
                messages.minOfOrNull { it.createdAt }?.toString()
            },
            onError = { throwable ->
                if (throwable is DataErrorException) {
                    _state.update { latestState ->
                        latestState.copy(
                            paginationError = throwable.error.asUiText(),
                        )
                    }
                }
            },
            onSuccess = { messages, _ ->
                _state.update { latestState ->
                    latestState.copy(
                        endReached = messages.isEmpty(),
                        paginationError = null,
                    )
                }
            }
        )

        _state.update {
            it.copy(
                endReached = false,
                isPaginationLoading = false,
            )
        }
    }
}
