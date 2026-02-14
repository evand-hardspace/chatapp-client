plugins {
    alias(libs.plugins.convention.kmpLibrary)
    alias(libs.plugins.convention.buildConfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)

                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.ktor.common)
                implementation(libs.touchlab.kermit)
                implementation(libs.koin.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.ktor.client.okhttp)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
