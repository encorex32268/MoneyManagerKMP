plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.readlkotlin)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(libs.library.base)
                implementation(libs.library.sync)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlin.date.time)
                implementation(libs.keysettings.kmp)
                implementation(libs.koin.core)
            }
        }
    }
}
