plugins {
    alias(libs.plugins.convention.kmpLibrary)
    alias(libs.plugins.convention.buildConfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.core.data)
                implementation(projects.client.core.presentation)
                implementation(projects.client.feature.chat.domain)
                implementation(projects.client.feature.chat.database)

                implementation(libs.kotlin.stdlib)

                implementation(libs.bundles.ktor.common)
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
                implementation(libs.koin.android)
                implementation(libs.androidx.lifecycle.process)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}
