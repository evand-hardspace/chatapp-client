plugins {
    alias(libs.plugins.convention.cmpLibrary)
}
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.common)
                implementation(projects.core.domain)
                implementation(libs.kotlin.stdlib)

                implementation(libs.jetbrains.compose.components.resources)
                implementation(libs.material3.adaptive)
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
