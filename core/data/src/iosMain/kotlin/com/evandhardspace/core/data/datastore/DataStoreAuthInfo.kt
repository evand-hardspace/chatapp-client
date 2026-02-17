@file:OptIn(ExperimentalForeignApi::class)

package com.evandhardspace.core.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import kotlinx.cinterop.ExperimentalForeignApi
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

fun createAuthInfoDataStore(): DataStore<Preferences> =
    createAuthInfoDataStore {
        val directory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        requireNotNull(directory).path + "/$DATA_STORE_AUTH_INFO_FILE_NAME"
    }
