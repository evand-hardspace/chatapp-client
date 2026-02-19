package com.evandhardspace.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class ResetPasswordRequest(
    val newPassword: String,
    val token: String,
)
