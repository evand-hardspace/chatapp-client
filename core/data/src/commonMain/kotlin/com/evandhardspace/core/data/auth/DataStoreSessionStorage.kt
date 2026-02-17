package com.evandhardspace.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.evandhardspace.core.data.mapper.toPreferences
import com.evandhardspace.core.domain.auth.AuthInfo
import com.evandhardspace.core.domain.auth.MutableSessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val AuthInfoKey = stringPreferencesKey("AUTH_INFO_KEY")

class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>,
    // TODO(6)
    private val json: Json,
): MutableSessionStorage {

    override val authInfo: Flow<AuthInfo?>
        get() = dataStore.data.map { prefs ->
            val authInfo = prefs[AuthInfoKey] ?: return@map null
            json.decodeFromString(authInfo)
        }

    override suspend fun saveAuthInfo(info: AuthInfo) {
        val serialized = json.encodeToString(info.toPreferences())
        dataStore.edit { pref ->
            pref[AuthInfoKey] = serialized
        }
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}