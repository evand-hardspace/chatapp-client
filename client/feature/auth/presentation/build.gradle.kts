plugins {
    alias(libs.plugins.convention.cmpFeature)
    alias(libs.plugins.convention.buildConfig)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.core.domain)
                implementation(projects.client.core.presentation)
                implementation(projects.client.core.designSystem)
                implementation(projects.client.core.urlUtil)
                implementation(projects.client.feature.auth.domain)

                implementation(libs.kotlin.stdlib)
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
