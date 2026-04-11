plugins {
    alias(libs.plugins.convention.cmpFeature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.client.core.domain)
                implementation(projects.client.core.designSystem)
                implementation(projects.client.feature.chat.domain)

                implementation(libs.kotlin.stdlib)
                implementation(libs.kotlinx.datetime)

                implementation(libs.material3.adaptive)
                implementation(libs.material3.adaptive.layout)
                implementation(libs.material3.adaptive.navigation)

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
