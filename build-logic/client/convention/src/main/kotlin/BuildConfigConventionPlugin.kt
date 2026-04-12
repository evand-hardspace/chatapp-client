import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import com.evandhardspace.chatapp.convention.extension.flavorStringProperty
import com.evandhardspace.chatapp.convention.pathToPackageName
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class BuildConfigConventionPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")
            }

            extensions.configure<BuildKonfigExtension> {
                packageName = target.pathToPackageName()

                val platformBaseUrl = project.flavorStringProperty("baseUrl")
                val platformBaseUrlWebSocket = project.flavorStringProperty("baseUrlWebSocket")

                targetConfigs {
                    create("android") {
                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "BASE_URL",
                            value = platformBaseUrl.android,
                        )
                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "BASE_URL_WEB_SOCKET",
                            value = platformBaseUrlWebSocket.android,
                        )
                    }
                    create("iosX64") {
                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "BASE_URL",
                            value = platformBaseUrl.ios,
                        )
                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "BASE_URL_WEB_SOCKET",
                            value = platformBaseUrlWebSocket.ios,
                        )
                    }
                    create("iosArm64") {
                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "BASE_URL",
                            value = platformBaseUrl.ios,
                        )
                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "BASE_URL_WEB_SOCKET",
                            value = platformBaseUrlWebSocket.ios,
                        )
                    }
                    create("iosSimulatorArm64") {
                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "BASE_URL",
                            value = platformBaseUrl.ios,
                        )
                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "BASE_URL_WEB_SOCKET",
                            value = platformBaseUrlWebSocket.ios,
                        )
                    }
                    defaultConfigs {
                        val apiKey = gradleLocalProperties(rootDir, rootProject.providers)
                            .getProperty("API_KEY").orEmpty() // TODO: replace orEmpty with add api key support

                        buildConfigField(
                            type = FieldSpec.Type.STRING,
                            name = "API_KEY",
                            value = apiKey,
                        )
                    }
                }
            }
        }
    }
}
