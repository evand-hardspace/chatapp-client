package com.evandhardspace.core.data.preferences

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoPreferences(
    val accessToken: String,
    val refreshToken: String,
    val user: UserPreferences,
)
