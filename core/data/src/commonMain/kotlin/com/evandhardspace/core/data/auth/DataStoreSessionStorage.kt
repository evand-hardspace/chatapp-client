package com.evandhardspace.core.data.auth

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.evandhardspace.core.data.mapper.toDomain
import com.evandhardspace.core.data.mapper.toPreferences
import com.evandhardspace.core.data.preferences.AuthInfoPreferences
import com.evandhardspace.core.domain.auth.AuthInfo
import com.evandhardspace.core.domain.auth.MutableSessionStorage
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json

private val AuthInfoKey = stringPreferencesKey("AUTH_INFO_KEY")

class DataStoreSessionStorage(
    private val dataStore: DataStore<Preferences>,
    // TODO(6)
    private val json: Json,
) : MutableSessionStorage {

    override fun authInfoFlow(): Flow<AuthInfo?> = dataStore.data.map { prefs ->
        val authInfo = prefs[AuthInfoKey] ?: return@map null
        json.decodeFromString<AuthInfoPreferences>(authInfo).toDomain()
    }

    override suspend fun saveAuthInfo(info: AuthInfo): AuthInfo {
        val serialized = json.encodeToString(info.toPreferences())
        // TODO(6): replace with updateData
        dataStore.edit { pref ->
            pref[AuthInfoKey] = serialized
        }
        return authInfoFlow().filterNotNull().first()
    }

    override suspend fun clear() {
        dataStore.edit { it.clear() }
    }
}