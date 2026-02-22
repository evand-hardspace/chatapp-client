package com.evandhardspace.url_util

import io.ktor.http.Url as KtorUrl

class Url(
    private val url: KtorUrl,
) {
    val parameters: List<Parameter>
        get() = buildList {
            url.parameters.forEach { key, values ->
                when (values.size) {
                    0 -> add(Parameter.EmptyParameter(key))
                    1 -> add(Parameter.SingleParameter(key, values.first()))
                    else -> add(Parameter.MultiParameter(key, values))
                }
            }
        }

    sealed interface Parameter {
        val key: String

        data class EmptyParameter(override val key: String) : Parameter

        data class SingleParameter(
            override val key: String,
            val values: String,
        ) : Parameter

        data class MultiParameter(
            override val key: String,
            val values: List<String>,
        ) : Parameter

        /**
         * returns the value of the parameter or null if the parameter is empty or has more than one value
         */
        fun getSingleOrNull(): String? = when (this) {
            is EmptyParameter -> null
            is SingleParameter -> values
            is MultiParameter -> null
        }
    }
}

fun String.asUrl(): Url = Url(KtorUrl(this))
