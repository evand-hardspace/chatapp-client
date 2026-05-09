plugins {
    alias(libs.plugins.convention.kmpLibrary)
    alias(libs.plugins.metro)
}
kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlin.stdlib)
                implementation(libs.jetbrains.lifecycle.viewmodel)
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

metro {
    generateContributionProviders = true
}
