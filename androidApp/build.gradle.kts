import java.util.Properties

plugins {
    alias(libs.plugins.convention.android.application.compose)
}

android {
    sourceSets["main"].assets.srcDir(
        project(":core:designsystem").layout.buildDirectory.dir("generated/compose/resourceGenerator/assembledResources/androidMain")
    )

    buildTypes {
        val properties = Properties()
        val localPropsFile = rootProject.file("local.properties")
        if (localPropsFile.exists()) {
            localPropsFile.inputStream().use { properties.load(it) }
        }

        getByName("debug") {
            val debugAppId = properties.getProperty("Ad_Android_ID_Debug").orEmpty()
            resValue(
                type = "string",
                name = "appId",
                value = if (debugAppId.startsWith("ca-app-pub-")) {
                    debugAppId
                } else {
                    "ca-app-pub-3940256099942544~3347511713"
                }
            )
        }
        getByName("release") {
            resValue(
                type = "string",
                name = "appId",
                value = properties.getProperty("Ad_Android_ID_Release") ?: ""
            )
        }
    }
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.core.splashscreen)
//    implementation(libs.google.playServices.ads)
    implementation(libs.filekit.core)
}

tasks.matching { it.name.startsWith("merge") && it.name.endsWith("Assets") }.configureEach {
    dependsOn(":core:designsystem:assembleAndroidMainResources")
}
