plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.room)
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
        androidMain {
            dependencies {
                implementation(libs.koin.android)
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
