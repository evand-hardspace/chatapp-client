package com.evandhardspace.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow

expect class AppLifecycleObserver {
    val isForeground: Flow<Boolean>
}
