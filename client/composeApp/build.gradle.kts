import com.evandhardspace.chatapp.convention.kotlinFreeCompileArgs
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.convention.cmpApplication)
    alias(libs.plugins.compose.hotReload)
    alias(libs.plugins.koin)
}

kotlin {
    compilerOptions {
        freeCompilerArgs.addAll(kotlinFreeCompileArgs)
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.jetbrains.compose.uiToolingPreview)
            implementation(libs.androidx.activity.compose)
            implementation(libs.core.splashscreen)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(projects.client.core.data)
            implementation(projects.client.core.domain)
            implementation(projects.client.core.common)
            implementation(projects.client.core.presentation)
            implementation(projects.client.core.designSystem)
            implementation(projects.client.core.urlUtil)
            implementation(projects.client.core.navigation)

            implementation(projects.client.feature.auth.domain)
            implementation(projects.client.feature.auth.presentation)

            implementation(projects.client.feature.chat.data)
            implementation(projects.client.feature.chat.database)
            implementation(projects.client.feature.chat.domain)
            implementation(projects.client.feature.chat.presentation)

            implementation(libs.bundles.koin.common)

            implementation(libs.jetbrains.compose.runtime)
            implementation(libs.jetbrains.compose.foundation)
            implementation(libs.jetbrains.compose.material3)
            implementation(libs.jetbrains.compose.ui)
            implementation(libs.jetbrains.compose.components.resources)
            implementation(libs.jetbrains.compose.uiToolingPreview)
            implementation(libs.jetbrains.compose.navigation)
            implementation(libs.jetbrains.lifecycle.viewmodel.compose)
            implementation(libs.jetbrains.lifecycle.runtime.compose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }
}

android {
    namespace = "com.evandhardspace.chatapp"

    defaultConfig {
        applicationId = "com.evandhardspace.chatapp"
    }

    sourceSets["debug"].manifest.srcFile("src/androidDebug/AndroidManifest.xml")
}

compose.desktop {
    application {
        mainClass = "com.evandhardspace.chatapp.MainKt"

        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "com.evandhardspace.chatapp"
            packageVersion = "1.0.0"
        }
    }
}
