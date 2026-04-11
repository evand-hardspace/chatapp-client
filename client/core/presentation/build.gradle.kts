plugins {
    alias(libs.plugins.convention.cmpLibrary)
}
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.client.core.common)
                implementation(projects.client.core.domain)

                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlin.stdlib)
                implementation(libs.jetbrains.compose.components.resources)
                implementation(libs.material3.adaptive)

                implementation(libs.bundles.koin.common)
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
