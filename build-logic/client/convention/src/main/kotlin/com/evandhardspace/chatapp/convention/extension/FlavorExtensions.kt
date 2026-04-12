package com.evandhardspace.chatapp.convention.extension

import org.gradle.api.Project

enum class Flavor(
    val value: String,
) {
    Local("local"),
    Dev("dev"),
    Prod("prod");

    companion object {
        fun fromString(value: String): Flavor = when (value) {
            Local.value -> Local
            Dev.value -> Dev
            Prod.value -> Prod
            else -> error("unknown flavor")
        }
    }
}

data class PlatformValue<T>(
    val android: T,
    val ios: T,
)

val Project.flavor: Flavor
    get() = properties["flavor"]?.let { flavor -> Flavor.fromString(flavor as String) }
        ?: Flavor.Dev

fun Project.flavorStringProperty(name: String): PlatformValue<String> {
    val flavorName = flavor.value

    val androidKey = "flavor.$flavorName@android.$name"
    val iosKey = "flavor.$flavorName@ios.$name"
    val defaultKey = "flavor.$flavorName.$name"

    return PlatformValue(
        android = (properties[androidKey] ?: properties[defaultKey]) as String,
        ios = (properties[iosKey] ?: properties[defaultKey]) as String,
    ).also { println(it) }
}
