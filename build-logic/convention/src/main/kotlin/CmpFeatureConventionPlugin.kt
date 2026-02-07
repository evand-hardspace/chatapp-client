import com.evandhardspace.chatapp.convention.extension.androidMainImplementation
import com.evandhardspace.chatapp.convention.extension.commonMainImplementation
import com.evandhardspace.chatapp.convention.extension.findPluginId
import com.evandhardspace.chatapp.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpFeatureConventionPlugin: Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("convention-cmpLibrary"))
            }

            dependencies {
                commonMainImplementation(project(":core:common"))
                commonMainImplementation(project(":core:presentation"))
                commonMainImplementation(project(":core:design-system"))

                commonMainImplementation(platform(libs.findLibrary("koin-bom").get()))
                androidMainImplementation(platform(libs.findLibrary("koin-bom").get()))

                commonMainImplementation(libs.findLibrary("koin-compose").get())
                commonMainImplementation(libs.findLibrary("koin-compose-viewmodel").get())

                commonMainImplementation(libs.findLibrary("jetbrains-compose-runtime").get())
                commonMainImplementation(libs.findLibrary("jetbrains-lifecycle-viewmodel-compose").get())
                commonMainImplementation(libs.findLibrary("jetbrains-lifecycle-viewmodel").get())
                commonMainImplementation(libs.findLibrary("jetbrains-lifecycle-runtime-compose").get())

                commonMainImplementation(libs.findLibrary("jetbrains-lifecycle-viewmodel-savedstate").get())
                commonMainImplementation(libs.findLibrary("jetbrains-savedstate").get())
                commonMainImplementation(libs.findLibrary("jetbrains-bundle").get())
                commonMainImplementation(libs.findLibrary("jetbrains-compose-navigation").get())

                commonMainImplementation(libs.findLibrary("jetbrains-compose-components-resources").get())

                androidMainImplementation(libs.findLibrary("koin-android").get())
                androidMainImplementation(libs.findLibrary("koin-androidx-compose").get())
                androidMainImplementation(libs.findLibrary("koin-androidx-navigation").get())
                androidMainImplementation(libs.findLibrary("koin-core-viewmodel").get())
            }
        }
    }
}
