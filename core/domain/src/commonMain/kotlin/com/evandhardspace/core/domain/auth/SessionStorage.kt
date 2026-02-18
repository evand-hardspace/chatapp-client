package com.evandhardspace.core.domain.auth

import kotlinx.coroutines.flow.Flow

interface SessionStorage {
    fun authInfoFlow(): Flow<AuthInfo?>
}

interface MutableSessionStorage: SessionStorage {
    suspend fun saveAuthInfo(info: AuthInfo): AuthInfo
    suspend fun clear()
}