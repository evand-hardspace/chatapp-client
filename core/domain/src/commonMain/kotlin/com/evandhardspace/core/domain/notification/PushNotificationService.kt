package com.evandhardspace.core.domain.notification

import kotlinx.coroutines.flow.Flow

interface PushNotificationService {
    val deviceToken: Flow<String?>
}
