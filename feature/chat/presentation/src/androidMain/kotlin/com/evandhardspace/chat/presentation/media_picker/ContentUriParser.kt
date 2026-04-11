package com.evandhardspace.chat.presentation.media_picker

import android.content.Context
import android.net.Uri
import android.webkit.MimeTypeMap
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class ContentUriParser(
    private val context: Context,
) {
    suspend fun readUri(uri: Uri): ByteArray? =
        withContext(Dispatchers.IO) {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                inputStream.readBytes()
            }
        }

    fun getMimeType(uri: Uri): String? =
        context.contentResolver.getType(uri)
            ?: getMimeTypeFromExtension(uri)

    private fun getMimeTypeFromExtension(uri: Uri): String? {
        val extension = uri.toString().substringAfterLast(".", "")
        return if (extension.isNotBlank()) {
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension)
        } else null
    }
}
