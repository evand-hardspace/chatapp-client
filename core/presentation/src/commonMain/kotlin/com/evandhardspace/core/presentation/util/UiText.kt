package com.evandhardspace.core.presentation.util

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.getString
import org.jetbrains.compose.resources.stringResource

sealed interface UiText {
    data class StringText(val value: String): UiText
    data class ResourceText(
        val id: StringResource,
        val args: Array<out Any> = arrayOf()
    ): UiText {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (other == null || this::class != other::class) return false

            other as ResourceText

            if (id != other.id) return false
            if (!args.contentEquals(other.args)) return false

            return true
        }

        override fun hashCode(): Int {
            var result = id.hashCode()
            result = 31 * result + args.contentHashCode()
            return result
        }
    }


    @Composable
    fun asComposableString(): String {
        return when(this) {
            is StringText -> value
            is ResourceText -> stringResource(
                resource = id,
                *args
            )
        }
    }

    suspend fun asString(): String {
        return when(this) {
            is StringText -> value
            is ResourceText -> getString(
                resource = id,
                *args
            )
        }
    }
}

fun StringResource.asUiText(): UiText = UiText.ResourceText(this)
fun StringResource.asUiText(vararg args: Any): UiText = UiText.ResourceText(this, args)
fun String.asUiText(): UiText = UiText.StringText(this)
