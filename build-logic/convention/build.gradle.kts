plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.jetbrains.compose.gradlePlugin)
    compileOnly(libs.buildkonfig.gradlePlugin)
    compileOnly(libs.buildkonfig.compiler)
}

gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "com.lihan.moneymanager.convention.android.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
        register("androidApplicationCompose") {
            id = "com.lihan.moneymanager.convention.android.application.compose"
            implementationClass = "AndroidApplicationComposeConventionPlugin"
        }
        register("cmpApplication") {
            id = "com.lihan.moneymanager.convention.cmp.application"
            implementationClass = "CmpApplicationConventionPlugin"
        }
        register("kmpLibrary") {
            id = "com.lihan.moneymanager.convention.kmp.library"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("cmpLibrary") {
            id = "com.lihan.moneymanager.convention.cmp.library"
            implementationClass = "CmpLibraryConventionPlugin"
        }
        register("cmpFeature") {
            id = "com.lihan.moneymanager.convention.cmp.feature"
            implementationClass = "CmpFeatureConventionPlugin"
        }
        register("buildkonfig") {
            id = "com.lihan.moneymanager.convention.buildkonfig"
            implementationClass = "BuildKonfigConventionPlugin"
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

kotlin {
    compilerOptions {
        jvmTarget.set(org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_17)
    }
}

tasks.validatePlugins {
    enableStricterValidation.set(true)
    failOnWarning.set(true)
}
