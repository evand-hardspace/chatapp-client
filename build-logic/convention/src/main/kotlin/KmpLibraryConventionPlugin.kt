import com.android.build.api.dsl.LibraryExtension
import com.evandhardspace.chatapp.convention.configureKotlinAndroid
import com.evandhardspace.chatapp.convention.configureKotlinMultiplatform
import com.evandhardspace.chatapp.convention.extension.commonMainImplementation
import com.evandhardspace.chatapp.convention.extension.commonTestImplementation
import com.evandhardspace.chatapp.convention.extension.libs
import com.evandhardspace.chatapp.convention.pathToResourcePrefix
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class KmpLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.android.library")
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.kotlin.plugin.serialization")
                apply("io.insert-koin.compiler.plugin")
            }

            configureKotlinMultiplatform()

            extensions.configure<LibraryExtension> {
                configureKotlinAndroid(this)

                resourcePrefix = this@with.pathToResourcePrefix()

                @Suppress("UnstableApiUsage")
                // Required to make debug build of app run in iOS simulator
                experimentalProperties["android.experimental.kmp.enableAndroidResources"] = "true"
            }

            dependencies {
                commonMainImplementation(libs.findLibrary("koin-annotations").get())
                commonMainImplementation(libs.findLibrary("koin-core").get())
                commonMainImplementation(libs.findLibrary("kotlinx-serialization-json").get())
                commonTestImplementation(libs.findLibrary("kotlin-test").get())
            }
        }
    }
}
