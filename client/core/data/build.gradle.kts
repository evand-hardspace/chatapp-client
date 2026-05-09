plugins {
    alias(libs.plugins.convention.kmpLibrary)
    alias(libs.plugins.convention.buildConfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.core.domain)
                implementation(projects.client.core.common)

                implementation(libs.kotlin.stdlib)
                implementation(libs.bundles.ktor.common)
                implementation(libs.touchlab.kermit)

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
            }
        }

        iosMain {
            dependencies {
                implementation(libs.ktor.client.darwin)
            }
        }
    }
}

metro {
    generateContributionProviders = true
}