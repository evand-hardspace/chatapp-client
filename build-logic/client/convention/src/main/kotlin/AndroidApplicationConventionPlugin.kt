import com.android.build.api.dsl.ApplicationExtension
import com.evandhardspace.chatapp.convention.configureKotlinAndroid
import com.evandhardspace.chatapp.convention.extension.findPluginId
import com.evandhardspace.chatapp.convention.extension.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin: Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply(libs.findPluginId("android-application"))
            }

            extensions.configure<ApplicationExtension> {

                defaultConfig {
                    targetSdk = libs.findVersion("projectTargetSdk").get().toString().toInt()
                    versionCode = libs.findVersion("projectVersionCode").get().toString().toInt()
                    versionName = libs.findVersion("projectVersionName").get().toString()
                }
                packaging {
                    resources {
                        excludes += "/META-INF/{AL2.0,LGPL2.1}"
                    }
                }
                buildTypes {
                    getByName("release") {
                        isMinifyEnabled = false
                    }
                }

                configureKotlinAndroid(this)
            }
        }
    }
}
