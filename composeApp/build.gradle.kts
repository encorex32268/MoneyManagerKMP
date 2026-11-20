plugins {
    alias(libs.plugins.convention.cmp.application)
}

compose.resources {
    packageOfResClass = "com.lihan.moneymanager.composeapp.generated.resources"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.data)
                implementation(projects.core.designsystem)
                implementation(projects.core.presentation)

                implementation(projects.feature.home.domain)
                implementation(projects.feature.home.data)
                implementation(projects.feature.home.presentation)

                implementation(projects.feature.chart.domain)
                implementation(projects.feature.chart.data)
                implementation(projects.feature.chart.presentation)

                implementation(projects.feature.analytics.domain)
                implementation(projects.feature.analytics.data)
                implementation(projects.feature.analytics.presentation)

                implementation(libs.bundles.koin.common)
                implementation(libs.androidx.navigation)
                implementation(libs.lifecycle.viewmodel.compose)
                implementation(libs.material3.windowsizeclass.multiplatform)
                implementation(libs.kotlinx.coroutines.core)
            }
        }
    }
}
