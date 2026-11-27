plugins {
    alias(libs.plugins.convention.kmp.library)
    alias(libs.plugins.convention.room)
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlin.date.time)
                implementation(libs.androidx.datastore.preferences)
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
