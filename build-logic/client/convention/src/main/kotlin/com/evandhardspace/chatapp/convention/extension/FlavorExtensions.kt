package com.evandhardspace.chatapp.convention.extension

import org.gradle.api.Project

enum class Flavor(
    val value: String,
) {
    Dev("dev"),
    Prod("prod");

    companion object {
        fun fromString(value: String): Flavor = when (value) {
            Dev.value -> Dev
            Prod.value -> Prod
            else -> error("unknown flavor")
        }
    }
}

val Project.flavor: Flavor
    get() = properties["flavor"]?.let { flavor -> Flavor.fromString(flavor as String) }
        ?: Flavor.Dev

fun Project.flavorStringProperty(name: String): String? {
    return properties["flavor.${flavor.value}.$name"] as? String
}
