package com.evandhardspace.core.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences

fun createAuthInfoDataStore(context: Context): DataStore<Preferences> =
    createAuthInfoDataStore {
        context.filesDir
            .resolve(DATA_STORE_AUTH_INFO_FILE_NAME)
            .absolutePath
    }
