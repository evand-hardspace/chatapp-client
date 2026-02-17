package com.evandhardspace.core.domain.auth

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    val authInfo: Flow<AuthInfo?>
}

interface MutableSessionStorage: SessionStorage {
    suspend fun saveAuthInfo(info: AuthInfo)
    suspend fun clear()
}