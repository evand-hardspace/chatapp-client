package com.evandhardspace.core.data.dto.request

import kotlinx.serialization.Serializable

@Serializable
data class EmailRequest(
    val email: String,
)
