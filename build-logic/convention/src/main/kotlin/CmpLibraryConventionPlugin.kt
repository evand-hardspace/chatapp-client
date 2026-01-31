import com.evandhardspace.chatapp.convention.extension.commonMainImplementation
import com.evandhardspace.chatapp.convention.extension.debugImplementation
import com.evandhardspace.chatapp.convention.extension.findPluginId
import com.evandhardspace.chatapp.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("convention-kmpLibrary"))
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                commonMainImplementation(libs.findLibrary("jetbrains-compose-ui").get())
                commonMainImplementation(libs.findLibrary("jetbrains-compose-foundation").get())
                commonMainImplementation(libs.findLibrary("jetbrains-compose-material3").get())
                commonMainImplementation(libs.findLibrary("jetbrains-compose-material-icons-core").get())

                debugImplementation(libs.findLibrary("androidx-compose-uiTooling").get())
            }
        }
    }
}