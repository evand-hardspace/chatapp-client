package com.evandhardspace.chat.presentation.media_picker

import androidx.compose.runtime.Composable

@Composable
expect fun rememberImagePickerLauncher(
    onResult: (PickedImageData) -> Unit,
): ImagePickerLauncher

class ImagePickerLauncher(
    private val onLaunch: () -> Unit,
) {
    fun launch(): Unit = onLaunch()
}

class PickedImageData(
    val bytes: ByteArray,
    val mimeType: String?,
)
