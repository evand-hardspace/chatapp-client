plugins {
    alias(libs.plugins.convention.kmpLibrary)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                api(projects.core.domain)

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
