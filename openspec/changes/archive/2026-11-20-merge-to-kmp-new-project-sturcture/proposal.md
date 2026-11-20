# Proposal: Migrate MoneyManagerKMP to Modular KMP Project Structure

## Why

MoneyManagerKMP currently houses all application, core, and feature code inside a single `:composeApp` module on Gradle 8.13, which prevents Gradle 9 / AGP 9 compatibility, mixes layer boundaries (e.g., `core.domain` importing `core.data` entities and Compose UI types), and duplicates build configuration. Restructuring the project according to the `kmp-*` architecture skills establishes clean module boundaries, reusable convention plugins, decoupled DI and navigation, and a maintainable multi-module baseline that compiles and runs on Android and iOS.

## What Changes

- **Build System & Gradle 9 Split (`:build-logic`, `:androidApp`, `:composeApp`)**:
  - **BREAKING**: Split `:composeApp` into a thin Android application shell (`:androidApp` with `com.android.application`) and a shared KMP library module (`:composeApp` with `com.android.kotlin.multiplatform.library`).
  - Introduce `:build-logic` included build with convention plugins (`kmp.library`, `cmp.library`, `cmp.feature`, `cmp.application`, `android.application`, `android.application.compose`, `buildkonfig`), aligned with `libs.versions.toml` and Realm Kotlin compatibility constraints.
  - **BREAKING**: Remove the incomplete `desktop` (`jvm("desktop")`) target so all modules target Android and iOS (`iosX64`, `iosArm64`, `iosSimulatorArm64`) consistently.
- **Core & Feature Modularization**:
  - Extract `:core:domain`, `:core:data`, `:core:presentation`, and `:core:designsystem` modules, resolving existing layer violations (moving Entity↔Domain mappers to `:core:data`, UI↔Domain mappers and `NavType`s to presentation, and theme/design tokens/shared components/Compose resources to `:core:designsystem` / `:core:presentation`).
  - Extract `:feature:home:{domain,data,presentation}`, `:feature:chart:{domain,data,presentation}`, and `:feature:analytics:{domain,data,presentation}` modules while keeping existing UI, ViewModel, and Realm persistence logic intact.
- **Dependency Injection (`kmp-di-koin`)**:
  - Replace the monolithic `feature.di.sharedModules` with per-module Koin modules (`coreDataModule`, `homeDataModule`, `homePresentationModule`, `chartDataModule`, `chartPresentationModule`, `analyticsDataModule`, `analyticsPresentationModule`, `appModule`) assembled via `initKoin()` in `:composeApp` and invoked from `:androidApp` (`MoneyManagerApp`) and `iosApp` (`iOSApp.swift`).
- **Navigation (`kmp-navigation`)**:
  - Move `@Serializable` route definitions out of `app.presentation.navigation.Route` into each feature's `presentation` module, expose `NavGraphBuilder.<feature>Graph(...)` extensions, and wire all graphs in `:composeApp`'s `NavHost`.

## Capabilities

### New Capabilities

- `build-system`: Gradle 9 / `:build-logic` convention plugins, version catalog management, `:androidApp` + `:composeApp` application/library split, BuildKonfig/AdMob config, and Android + iOS target configuration.
- `module-architecture`: Multi-module KMP layout (`:core:*` and `:feature:*:{domain,data,presentation}`), strict dependency direction rules, Compose Resources sharing, and platform `expect`/`actual` placement.
- `dependency-injection`: Layer-scoped Koin DI modules assembled via `initKoin` in `:composeApp` and initialized from `:androidApp` and `iosApp`.
- `navigation-architecture`: Feature-owned `@Serializable` routes and `NavGraphBuilder` graph extensions wired in `:composeApp` without cross-feature dependencies.

### Modified Capabilities

<!-- None: no existing OpenSpec specs exist in openspec/specs/. -->

## Impact

- **Build & Tooling**: `settings.gradle.kts`, `build.gradle.kts`, `gradle/libs.versions.toml`, `gradle/wrapper/gradle-wrapper.properties`, new `build-logic/` and `androidApp/` directories.
- **Source Code**: All Kotlin source sets, `composeResources`, and AndroidManifest/resources currently under `composeApp/src/` will be relocated into their respective `:androidApp`, `:core:*`, `:feature:*`, and `:composeApp` modules; `package` declarations and imports (including `Res` imports) will be updated where required by module boundaries.
- **iOS Integration**: `iosApp/iosApp/iOSApp.swift` and `ContentView.swift` updated if needed for `initKoin()` / `ComposeApp` framework export.
- **Persistence**: Existing MongoDB Realm entities, `RealmFactory`, and `RealmMigration` are preserved in `:core:data` so existing user data remains intact.
