package com.evandhardspace.core.data.auth.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String,
)
