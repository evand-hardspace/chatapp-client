package com.evandhardspace.core.data.mapper

import com.evandhardspace.core.data.dto.AuthInfoResource
import com.evandhardspace.core.data.dto.UserResource
import com.evandhardspace.core.domain.auth.AuthInfo
import com.evandhardspace.core.domain.auth.User

fun AuthInfoResource.toDomain(): AuthInfo = AuthInfo(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toDomain(),
)

fun UserResource.toDomain(): User = User(
    id = id,
    email = email,
    username = username,
    hasVerifiedEmail = hasVerifiedEmail,
    profilePictureUrl = profilePictureUrl,
)
