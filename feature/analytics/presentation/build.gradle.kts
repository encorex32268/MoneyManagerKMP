plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.feature.analytics.domain)
                implementation(libs.csv)
                implementation(libs.filekit.core)
                implementation(libs.filekit.compose)
            }
        }
    }
}
