import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.compose.internal.utils.localPropertiesFile
import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.util.Properties

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    alias(libs.plugins.readlkotlin)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.konfigplugin)

}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider.configure {
                compilerOptions {
                    jvmTarget.set(JvmTarget.JVM_11)
                }
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    jvm("desktop")
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(libs.androidx.activity.compose)
            implementation(libs.koin.android)
            implementation(libs.core.splashscreen)
            implementation(libs.google.playServices.ads)
            implementation(compose.preview)
            implementation(compose.components.resources)
            implementation(compose.material3)

        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)

            implementation(libs.library.base)
            implementation(libs.library.sync)
            
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.kotlin.date.time)

            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)

            implementation(libs.androidx.navigation)
            implementation(libs.uri.kmp)
            implementation(libs.lifecycle.viewmodel.compose)
            implementation(libs.compose.colorpicker)
            implementation(libs.keysettings.kmp)

            implementation(libs.filekit.core)
            implementation(libs.filekit.compose)
            implementation(libs.csv)

            implementation(libs.material3.windowsizeclass.multiplatform)



        }
        commonTest.dependencies {
            implementation(libs.koin.test)

        }
    }
}

android {
    namespace = "com.lihan.moneymanager"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "com.lihan.moneymanager"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 22
        versionName = "1.3.8"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        val properties = Properties()
        properties.load(
            project.rootProject.localPropertiesFile.inputStream()
        )

        getByName("debug"){
            resValue(
                type = "string",
                name = "appId",
                value = properties.getProperty("Ad_Android_ID_Debug")
            )
        }
        getByName("release") {
            isMinifyEnabled = false
            resValue(
                type = "string",
                name = "appId",
                value = properties.getProperty("Ad_Android_ID_Release")
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

buildkonfig {
    packageName = "com.lihan.moneymanager"
    val properties = Properties()
    properties.load(
        project.rootProject.localPropertiesFile.inputStream()
    )
    defaultConfigs {
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "AdUnitID",
            value = properties.getProperty("AdUnitID_Android_Debug")
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "AdUnitID_iOS",
            value = properties.getProperty("AdUnitID_iOS")
        )
    }
    defaultConfigs("release"){
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "AdUnitID",
            value = properties.getProperty("AdUnitID_Android_Release")
        )
        buildConfigField(
            type = FieldSpec.Type.STRING,
            name = "AdUnitID_iOS",
            value = properties.getProperty("AdUnitID_iOS")
        )
    }
}

dependencies {
    debugImplementation(compose.uiTooling)
}

task("testClasses")



