plugins {
    alias(libs.plugins.convention.kmp.library)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(projects.core.data)
                implementation(projects.feature.analytics.domain)
                implementation(libs.koin.core)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlin.date.time)
                implementation(libs.csv)
                implementation(libs.filekit.core)
            }
        }
        commonTest {
            dependencies {
                implementation(projects.core.domain)
                implementation(libs.kotlinx.coroutines.test)
            }
        }
    }
}
