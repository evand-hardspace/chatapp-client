import com.android.build.api.dsl.ApplicationExtension
import com.evandhardspace.chatapp.convention.configureAndroidCompose
import com.evandhardspace.chatapp.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPlugin("convention-androidApplication").get().get().pluginId)
                apply(libs.findPlugin("compose-compiler").get().get().pluginId)
            }

            val extension = extensions.getByType<ApplicationExtension>()
            configureAndroidCompose(extension)
        }
    }
}
