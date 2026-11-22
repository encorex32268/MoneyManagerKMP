import java.util.Properties

plugins {
    alias(libs.plugins.convention.android.application.compose)
}

android {
    sourceSets["main"].assets.srcDir(
        project(":core:designsystem").layout.buildDirectory.dir("generated/compose/resourceGenerator/assembledResources/androidMain")
    )
}

dependencies {
    implementation(projects.composeApp)
    implementation(libs.androidx.activity.compose)
    implementation(libs.koin.android)
    implementation(libs.core.splashscreen)
    implementation(libs.filekit.core)
}

tasks.matching { it.name.startsWith("merge") && it.name.endsWith("Assets") }.configureEach {
    dependsOn(":core:designsystem:assembleAndroidMainResources")
}
