plugins {
    alias(libs.plugins.convention.cmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.designsystem)
                implementation(libs.kotlin.date.time)
                implementation(libs.androidx.navigation)
                implementation(libs.uri.kmp)
                implementation(libs.material3.windowsizeclass.multiplatform)
                implementation(libs.filekit.core)
            }
        }
    }
}
