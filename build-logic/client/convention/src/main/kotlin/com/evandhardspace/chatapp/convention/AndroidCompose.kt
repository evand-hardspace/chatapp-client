package com.evandhardspace.chatapp.convention

import com.android.build.api.dsl.CommonExtension
import com.evandhardspace.chatapp.convention.extension.debugImplementation
import com.evandhardspace.chatapp.convention.extension.implementation
import com.evandhardspace.chatapp.convention.extension.libs
import com.evandhardspace.chatapp.convention.extension.testImplementation
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>
) {
    with(commonExtension) {
        buildFeatures {
            compose = true
        }

        dependencies {
            val bom = libs.findLibrary("androidx-compose-bom").get()
            implementation(platform(bom))
            testImplementation(platform(bom))
            debugImplementation(libs.findLibrary("androidx-compose-uiToolingPreview").get())
            debugImplementation(libs.findLibrary("androidx-compose-uiTooling").get())
        }
    }
}
