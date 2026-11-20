import com.codingfeline.buildkonfig.compiler.FieldSpec
import com.codingfeline.buildkonfig.gradle.BuildKonfigExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import java.util.Properties

class BuildKonfigConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.codingfeline.buildkonfig")
            }

            extensions.configure<BuildKonfigExtension> {
                packageName = "com.lihan.moneymanager"
                val properties = Properties()
                val localPropsFile = rootProject.file("local.properties")
                if (localPropsFile.exists()) {
                    localPropsFile.inputStream().use { properties.load(it) }
                }

                defaultConfigs {
                    buildConfigField(
                        type = FieldSpec.Type.STRING,
                        name = "AdUnitID",
                        value = properties.getProperty("AdUnitID_Android_Debug") ?: ""
                    )
                    buildConfigField(
                        type = FieldSpec.Type.STRING,
                        name = "AdUnitID_iOS",
                        value = properties.getProperty("AdUnitID_iOS") ?: ""
                    )
                }
                defaultConfigs("release") {
                    buildConfigField(
                        type = FieldSpec.Type.STRING,
                        name = "AdUnitID",
                        value = properties.getProperty("AdUnitID_Android_Release") ?: ""
                    )
                    buildConfigField(
                        type = FieldSpec.Type.STRING,
                        name = "AdUnitID_iOS",
                        value = properties.getProperty("AdUnitID_iOS") ?: ""
                    )
                }
            }
        }
    }
}
