plugins {
    alias(libs.plugins.convention.cmpLibrary)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.common)

                implementation(libs.kotlin.stdlib)

                api(libs.jetbrains.compose.navigation)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}
