package com.evandhardspace.chat.data.lifecycle

import kotlinx.coroutines.flow.Flow
import org.koin.core.annotation.Single

@Single
expect class AppLifecycleObserver {
    val isForeground: Flow<Boolean>
}
