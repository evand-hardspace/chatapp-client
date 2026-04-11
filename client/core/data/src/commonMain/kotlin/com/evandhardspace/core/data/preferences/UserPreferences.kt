package com.evandhardspace.core.data.preferences

import kotlinx.serialization.Serializable

@Serializable
data class UserPreferences(
    val id: String,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
    val profilePictureUrl: String?,
)
