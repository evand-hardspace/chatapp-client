plugins {
    alias(libs.plugins.convention.cmpFeature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.designSystem)
                implementation(projects.feature.chat.domain)

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
