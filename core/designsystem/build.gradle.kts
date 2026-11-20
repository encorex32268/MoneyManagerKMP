plugins {
    alias(libs.plugins.convention.cmp.library)
    alias(libs.plugins.convention.buildkonfig)
}

compose.resources {
    publicResClass = true
    packageOfResClass = "moneymanagerkmp.composeapp.generated.resources"
}

kotlin {
    sourceSets {
        commonMain {
            dependencies {
                implementation(projects.core.domain)
                implementation(libs.kotlinx.coroutines.core)
                implementation(libs.kotlin.date.time)
            }
        }
        androidMain {
            dependencies {
//                implementation(libs.google.playServices.ads)
            }
        }
    }
}
