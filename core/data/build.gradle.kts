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

                implementation(libs.datastore)
                implementation(libs.datastore.preferences)
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
                implementation(libs.koin.android)
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}
