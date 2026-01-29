import com.android.build.api.dsl.ApplicationExtension
import com.evandhardspace.chatapp.convention.configureAndroidCompose
import com.evandhardspace.chatapp.convention.extension.findPluginId
import com.evandhardspace.chatapp.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("convention-androidApplication"))
                apply("org.jetbrains.kotlin.plugin.compose")
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}
