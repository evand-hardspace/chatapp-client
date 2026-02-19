package com.evandhardspace.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.evandhardspace.core.data.mapper.toDomain
import com.evandhardspace.core.data.mapper.toPreferences
import com.evandhardspace.core.data.preferences.AuthInfoPreferences
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.MutableSessionRepository
import com.evandhardspace.core.domain.auth.SessionEvents
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val AuthInfoKey = stringPreferencesKey("AUTH_INFO_KEY")

class DataStoreSessionRepository(
    private val dataStore: DataStore<Preferences>,
    // TODO(6)
    private val json: Json,
) : MutableSessionRepository {

    private val _events = MutableSharedFlow<SessionEvents>()
    override val events: SharedFlow<SessionEvents> = _events.asSharedFlow()
    override val authState: Flow<AuthState> = dataStore.data.map { prefs ->
        val authInfo = prefs[AuthInfoKey] ?: return@map AuthState.Unauthorized
        json.decodeFromString<AuthInfoPreferences>(authInfo).toDomain()
    }

    override suspend fun saveAuthInfo(info: AuthState.Authorized): AuthState.Authorized {
        val serialized = json.encodeToString(info.toPreferences())
        // TODO(6): replace with updateData
        dataStore.edit { pref ->
            pref[AuthInfoKey] = serialized
        }
        val result = authState.first()
        require(result is AuthState.Authorized) { "authState should emit a state that was just saved" }
        return result
    }

    override suspend fun logout() {
        dataStore.edit { it.clear() }
        _events.emit(SessionEvents.LoggedOut)
    }
}
