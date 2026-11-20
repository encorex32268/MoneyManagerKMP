plugins {
    alias(libs.plugins.convention.cmp.feature)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.feature.chart.domain)
                implementation(libs.compose.charts)
            }
        }
    }
}
