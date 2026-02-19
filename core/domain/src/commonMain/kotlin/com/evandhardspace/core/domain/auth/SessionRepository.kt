package com.evandhardspace.core.domain.auth

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharedFlow

interface SessionRepository {

    val authState: Flow<AuthState>

    val events: SharedFlow<SessionEvents>
}

interface MutableSessionRepository: SessionRepository {
    suspend fun saveAuthInfo(info: AuthState.Authorized): AuthState.Authorized
    suspend fun logout()
}

sealed interface SessionEvents {
    data object LoggedOut: SessionEvents
}
