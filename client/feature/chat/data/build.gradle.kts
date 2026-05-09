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
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlin.test)
            }
        }

        androidMain {
            dependencies {
                implementation(libs.androidx.core.ktx)
                implementation(libs.androidx.lifecycle.process)
            }
        }

        iosMain {
            dependencies {
            }
        }
    }
}

metro {
    generateContributionProviders = true
}