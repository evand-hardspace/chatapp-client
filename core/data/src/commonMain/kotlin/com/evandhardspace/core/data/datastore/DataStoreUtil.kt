package com.evandhardspace.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import okio.Path.Companion.toPath

fun createAuthInfoDataStore(producePath: () -> String): DataStore<Preferences> =
    PreferenceDataStoreFactory.createWithPath { producePath().toPath() }

internal const val DATA_STORE_AUTH_INFO_FILE_NAME = "auth_info.preferences_pb"