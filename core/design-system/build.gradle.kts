plugins {
    alias(libs.plugins.convention.cmpLibrary)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.presentation)

                api(libs.material3.adaptive)
                api(libs.material3.adaptive.layout)
                api(libs.material3.adaptive.navigation)

                api(libs.jetbrains.compose.navigationEvent)

                implementation(libs.kotlin.stdlib)
                implementation(libs.jetbrains.compose.components.resources)
                implementation(libs.jetbrains.compose.uiToolingPreview)
                implementation(libs.coil.compose)
                implementation(libs.coil.network.ktor)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.activity.compose)
            }
        }
        iosMain {
            dependencies {
            }
        }
    }
}

compose.resources {
    publicResClass = true
}