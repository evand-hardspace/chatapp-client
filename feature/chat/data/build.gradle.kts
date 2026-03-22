plugins {
    alias(libs.plugins.convention.kmpLibrary)
    alias(libs.plugins.convention.buildConfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.data)
                implementation(projects.core.presentation)
                implementation(projects.feature.chat.domain)
                implementation(projects.feature.chat.database)

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
