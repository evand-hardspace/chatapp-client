package com.evandhardspace.core.data.dto

import kotlinx.serialization.Serializable

@Serializable
data class AuthInfoResource(
    val accessToken: String,
    val refreshToken: String,
    val user: UserResource,
)
