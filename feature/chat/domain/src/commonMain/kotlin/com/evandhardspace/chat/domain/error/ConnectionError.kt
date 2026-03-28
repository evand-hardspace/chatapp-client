package com.evandhardspace.chat.domain.error

import com.evandhardspace.core.domain.util.DomainError

enum class ConnectionError: DomainError {
    NotConnected,
    MessageSendFailed,
}