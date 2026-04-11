package com.evandhardspace.core.navigation.deeplink

import androidx.navigation.NavController

fun interface DeeplinkInterceptor {
    suspend fun process(uri: String, navController: NavController): Boolean

    fun String.startsWith(vararg scheme: String): Boolean {
        for (s in scheme) {
            if (startsWith(prefix = s)) return true
        }
        return false
    }
}
