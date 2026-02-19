package com.evandhardspace.core.data.mapper

import com.evandhardspace.core.data.dto.AuthInfoDto
import com.evandhardspace.core.data.dto.UserDto
import com.evandhardspace.core.data.preferences.AuthInfoPreferences
import com.evandhardspace.core.data.preferences.UserPreferences
import com.evandhardspace.core.domain.auth.AuthState
import com.evandhardspace.core.domain.auth.User

internal fun AuthInfoDto.toDomain() = AuthState.Authorized(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toDomain(),
)

internal fun AuthInfoPreferences.toDomain() = AuthState.Authorized(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toDomain(),
)

internal fun AuthState.Authorized.toPreferences(): AuthInfoPreferences = AuthInfoPreferences(
    accessToken = accessToken,
    refreshToken = refreshToken,
    user = user.toPreferences(),
)

internal fun UserDto.toDomain(): User = User(
    id = id,
    email = email,
    username = username,
    hasVerifiedEmail = hasVerifiedEmail,
    profilePictureUrl = profilePictureUrl,
)

internal fun UserPreferences.toDomain(): User = User(
    id = id,
    email = email,
    username = username,
    hasVerifiedEmail = hasVerifiedEmail,
    profilePictureUrl = profilePictureUrl,
)

internal fun User.toPreferences(): UserPreferences = UserPreferences(
    id = id,
    email = email,
    username = username,
    hasVerifiedEmail = hasVerifiedEmail,
    profilePictureUrl = profilePictureUrl,
)
