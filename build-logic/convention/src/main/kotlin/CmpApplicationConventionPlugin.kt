import com.evandhardspace.chatapp.convention.configureAndroidTarget
import com.evandhardspace.chatapp.convention.configureIosTargets
import com.evandhardspace.chatapp.convention.extension.debugImplementation
import com.evandhardspace.chatapp.convention.extension.findPluginId
import com.evandhardspace.chatapp.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpApplicationConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("convention-androidApplicationCompose"))
                apply("org.jetbrains.kotlin.multiplatform")
                apply("org.jetbrains.compose")
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            configureAndroidTarget()
            configureIosTargets()
            // TODO(1): Add jvm and js targets
//            configureJvmTarget()
//            configureJsTarget()
//            configureWasmJsTarget()

            dependencies {
                debugImplementation(libs.findLibrary("androidx-compose-ui-tooling").get())
            }
        }
    }
}
