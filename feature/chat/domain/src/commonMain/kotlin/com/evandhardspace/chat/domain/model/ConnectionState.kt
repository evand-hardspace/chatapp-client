package com.evandhardspace.chat.domain.model

enum class ConnectionState {
    Disconnected,
    Connecting,
    Connected,
    ErrorNetwork,
    ErrorUnknown;

    val isConnected: Boolean
        get() = this == Connected
}
