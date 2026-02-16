package com.evandhardspace.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class UserResource(
    val id: String,
    val email: String,
    val username: String,
    val hasVerifiedEmail: Boolean,
    val profilePictureUrl: String?,
)
