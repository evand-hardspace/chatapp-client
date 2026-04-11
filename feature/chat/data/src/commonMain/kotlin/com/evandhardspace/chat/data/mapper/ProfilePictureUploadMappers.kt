package com.evandhardspace.chat.data.mapper

import com.evandhardspace.chat.data.dto.response.ProfilePictureUploadUrlsResponse
import com.evandhardspace.chat.domain.model.ProfilePictureUploadUrls

fun ProfilePictureUploadUrlsResponse.toDomain(): ProfilePictureUploadUrls =
    ProfilePictureUploadUrls(
        uploadUrl = uploadUrl,
        publicUrl = publicUrl,
        headers = headers,
    )
