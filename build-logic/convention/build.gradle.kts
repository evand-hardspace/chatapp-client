import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    `kotlin-dsl`
}

group = "com.evandhardspace.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
    compileOnly(libs.androidx.room.gradle.plugin)
    implementation(libs.buildkonfig.gradlePlugin)
    implementation(libs.buildkonfig.compiler)
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget = JvmTarget.JVM_17
    }
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = libs.plugins.convention.androidApplication.get().pluginId
            implementationClass = "AndroidApplicationConventionPlugin"
        }

        register("androidApplicationCompose") {
            id = libs.plugins.convention.androidApplicationCompose.get().pluginId
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }

        register("cmpApplication") {
            id = libs.plugins.convention.cmpApplication.get().pluginId
            implementationClass = "CmpApplicationConventionPlugin"
        }

        register("kmpLibrary") {
            id = libs.plugins.convention.kmpLibrary.get().pluginId
            implementationClass = "KmpLibraryConventionPlugin"
        }

        register("cmpLibrary") {
            id = libs.plugins.convention.cmpLibrary.get().pluginId
            implementationClass = "CmpLibraryConventionPlugin"
        }

        register("cmpFeature") {
            id = libs.plugins.convention.cmpFeature.get().pluginId
            implementationClass = "CmpFeatureConventionPlugin"
        }

        register("buildConfig") {
            id = libs.plugins.convention.buildConfig.get().pluginId
            implementationClass = "BuildConfigConventionPlugin"
        }

        register("room") {
            id = libs.plugins.convention.room.get().pluginId
            implementationClass = "RoomConventionPlugin"
        }
    }
}
