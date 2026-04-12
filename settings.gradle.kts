rootProject.name = "Chatapp"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    includeBuild("build-logic")
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google {
            mavenContent {
                includeGroupAndSubgroups("androidx")
                includeGroupAndSubgroups("com.android")
                includeGroupAndSubgroups("com.google")
            }
        }
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

include(":client:composeApp")
include(":client:core:presentation")
include(":client:core:domain")
include(":client:core:data")
include(":client:core:design-system")
include(":client:core:common")
include(":client:core:navigation")
include(":client:core:url-util")
include(":client:feature:auth:presentation")
include(":client:feature:auth:domain")
include(":client:feature:chat:presentation")
include(":client:feature:chat:domain")
include(":client:feature:chat:data")
include(":client:feature:chat:database")
