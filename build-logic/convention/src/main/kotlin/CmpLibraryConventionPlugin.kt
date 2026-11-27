import com.lihan.moneymanager.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class CmpLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            with(pluginManager) {
                apply("com.lihan.moneymanager.convention.kmp.library")
                apply("org.jetbrains.kotlin.plugin.compose")
                apply("org.jetbrains.compose")
            }

            dependencies {
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-foundation").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material3").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-material-icons-core").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-runtime").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-components-resources").get())
                "commonMainImplementation"(libs.findLibrary("jetbrains-compose-ui-tooling-preview").get())

                // Android previews for com.android.kotlin.multiplatform.library require androidRuntimeClasspath.
                "androidRuntimeClasspath"(libs.findLibrary("jetbrains-compose-ui-tooling").get())
                "androidRuntimeClasspath"(libs.findLibrary("androidx-customview-poolingcontainer").get())
                "androidMainImplementation"(platform(libs.findLibrary("androidx-compose-bom").get()))
                "androidMainImplementation"(libs.findLibrary("androidx-compose-ui-tooling").get())
                "androidMainImplementation"(libs.findLibrary("androidx-compose-ui-tooling-preview").get())
            }
        }
    }
}
