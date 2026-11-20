# Spec Delta

## Purpose

Defines the multi-module Gradle build system, convention plugins, version catalog, and Android/iOS target configuration for MoneyManagerKMP.

## ADDED Requirements

### Requirement: Android application and KMP library split
The build system SHALL separate the Android application shell (`:androidApp`) from the shared Compose Multiplatform library (`:composeApp`), where `:androidApp` applies the Android application plugin and depends on `:composeApp`, and `:composeApp` applies the KMP Android library configuration.

#### Scenario: Building Android application artifact
- **WHEN** the Android debug or release build task is executed
- **THEN** `:androidApp` packages the Android APK/AAB with `applicationId` `com.lihan.moneymanager`, AdMob `appId` resources from `local.properties`, and the compiled `:composeApp` shared library

### Requirement: Centralized convention plugins via build-logic
The build system SHALL provide an included `:build-logic` build defining convention plugins for KMP libraries, Compose Multiplatform libraries, Compose feature modules, the Compose umbrella module, the Android application module, and BuildKonfig configuration.

#### Scenario: Module applies convention plugin
- **WHEN** a `:core:*` or `:feature:*` module applies a convention plugin from `libs.versions.toml`
- **THEN** target platforms, Java 17 / JVM toolchain settings, compiler opt-ins, and shared dependencies are configured without duplicating boilerplate in the module build script

### Requirement: Android and iOS target platforms only
The build system SHALL configure Android and iOS (`iosX64`, `iosArm64`, `iosSimulatorArm64`) targets across all KMP modules and SHALL NOT configure a Desktop JVM target.

#### Scenario: Compiling KMP modules
- **WHEN** Gradle compiles shared KMP modules for Android or iOS
- **THEN** Android and iOS source sets compile without requiring `desktopMain` `actual` implementations

### Requirement: Externalized secrets and AdMob configuration
The build system SHALL read AdMob IDs and unit IDs from `local.properties` via BuildKonfig and Android build-type `resValue` entries without hardcoding secrets in source or build files.

#### Scenario: Debug vs release AdMob configuration
- **WHEN** building debug or release variants
- **THEN** `BuildKonfig` provides `AdUnitID` and `AdUnitID_iOS` and `:androidApp` provides `appId` matching the corresponding `local.properties` entries
