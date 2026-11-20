# Design: Modular KMP Project Structure

## Context

MoneyManagerKMP is a Kotlin Multiplatform (Android + iOS) personal finance app using Compose Multiplatform, Koin, JetBrains Compose Navigation, BuildKonfig, and MongoDB Realm (`io.realm.kotlin`). Currently all code resides in a single `:composeApp` module (see `proposal.md` for motivation). While packages are already named `app`, `core.*`, and `feature.{home,chart,analytics}.*`, Gradle-level module boundaries are absent and several cross-layer imports exist (e.g., `core.domain.mapper` importing `core.data.model.*Entity` and `core.presentation.model.CategoryUi`; `feature.home.domain` importing `NavType`s; `app.presentation.navigation.Route` centralizing all feature routes).

## Goals / Non-Goals

**Goals:**
- Implement the `:build-logic` + `:androidApp` + `:composeApp` + `:core:*` + `:feature:*` module layout from `kmp-module-structure` targeting Android and iOS (`iosX64`, `iosArm64`, `iosSimulatorArm64`).
- Enforce unidirectional Clean Architecture dependencies at the Gradle module level.
- Adopt `kmp-di-koin` (`initKoin()` in `:composeApp`, layer-scoped Koin modules) and `kmp-navigation` (feature-owned `@Serializable` routes and `NavGraphBuilder.<feature>Graph(...)`).
- Preserve existing Realm schemas, migrations, UI screens, ViewModels, and AdMob/BuildKonfig behavior so the app compiles and runs on Android and iOS without data loss.

**Non-Goals:**
- Migrating MongoDB Realm to Room KMP (deferred to a separate change to protect existing user data and avoid Kotlin 2.2+ upgrade blockers).
- Refactoring existing ViewModels/Screens to `kmp-presentation-mvi` or introducing `Result<T, DataError>` from `kmp-error-handling`.
- Completing or maintaining the `desktop` (`jvm("desktop")`) target.

## Decisions

### 1. Gradle / AGP / Kotlin / Realm Version Compatibility
- **Decision**: Use `com.android.kotlin.multiplatform.library` for all KMP library modules (`:core:*`, `:feature:*`, `:composeApp`) and `com.android.application` for `:androidApp`. Keep `kotlin = "2.0.10"` (or `2.0.x`) and `io.realm.kotlin` (`2.1.0` / `2.3.0`), using Gradle 9 if compatible with AGP 8.11.2+ and Realm `2.1.0`, or Gradle 8.13/8.14 if Realm's Gradle plugin fails under Gradle 9.
- **Rationale**: `io.realm.kotlin` 2.1.0/2.3.0 is compiled against Kotlin 2.0.x and is not compatible with Kotlin 2.1+/2.2+. AGP `8.11.2` already provides `com.android.kotlin.multiplatform.library`.
- **Alternatives considered**: Upgrading to Kotlin 2.2+ and migrating Realm to Room KMP — rejected for this change per user decision to preserve Realm and existing data.

### 2. Target Module Graph
- `:androidApp` (`android.application.compose`): `MainActivity`, `MoneyManagerApp`, `AndroidManifest.xml`, launcher/splash resources, AdMob `appId` `resValue`s. Depends only on `:composeApp`.
- `:composeApp` (`cmp.application`): `App.kt`, `AppTheme.kt`, `AppViewModel.kt`, `AppState.kt`, `AppEvent.kt`, `initKoin()`, `MainViewController.kt`, top-level `NavHost` wiring. Depends on all `:core:*` and `:feature:*` modules.
- `:core:domain` (`kmp.library`): Domain models (`Category`, `Expense`, `SpendingLimit`, `Type`, `Chart`), repository interfaces (`ExpenseRepository`, `SpendingLimitRepository`, `TypeRepository`), `KeySettings`, `CategoryData`, `exts/`, `ZeroStringDisplay`, `AppLogger` (expect/actual), `UUID` (expect/actual), `Formatter` (expect/actual), `GetDownloadsPath` (expect/actual), `FileLogger`.
- `:core:data` (`kmp.library` + `io.realm.kotlin`): Realm entities (`CategoryEntity`, `ExpenseEntity`, `SpendingLimitEntity`, `TypeEntity`), `RealmFactory`, `RealmMigration`, `MongoDB`, `DefaultKeySettings`, core repository implementations, and **Entity↔Domain mappers** (moved out of `core.domain.mapper`). Depends on `:core:domain`.
- `:core:designsystem` (`cmp.library`): `Color.kt`, `AppTypography.kt`, shared `composeResources` (with `publicResClass = true` and `packageOfResClass = "moneymanagerkmp.composeapp.generated.resources"`), reusable UI components (`CircleIcon`, `DatePicker`, `DeleteDialog`, `OneButtonDialog`, `SpendingLimitsProgressBar`, `AdMobBanner` expect/actual with `buildkonfig` for `AdUnitID`/`AdUnitID_iOS`).
- `:core:presentation` (`cmp.library`): `ObserveAsEvents`, `UiText`, `ModifierExt`, `DateConverter`, `CategoryList`, `CategoryUi` + UI↔Domain mappers (moved out of `core.domain.mapper`), `GetScreenSize` (expect/actual), `NavigationLayoutType`, `AppNavigationBottom`/`AppNavigationRail` (decoupled from feature routes), and shared `NavType`s (`ExpenseNavType`, `ExpenseListNavType`, `TypeNavType`). Depends on `:core:domain` and `:core:designsystem`.
- `:feature:<name>:domain` (`kmp.library`) for `home`, `chart`, `analytics`: Feature repository interfaces and domain models/utils (`DisplayDayString`, `BackupData`, `BackupResult`). Depends only on `:core:domain`.
- `:feature:<name>:data` (`kmp.library`) for `home`, `chart`, `analytics`: Feature repository implementations (`HomeRepositoryImpl` if any / `ChartRepositoryImpl`, `AnalyticsRepositoryImpl`, `BackupRepositoryImpl`) and `feature*DataModule`. Depends on own `domain`, `:core:domain`, `:core:data`.
- `:feature:<name>:presentation` (`cmp.feature`) for `home`, `chart`, `analytics`: ViewModels, Screens, States, Events, UI models/mappers, `@Serializable` route objects, `NavGraphBuilder.<feature>Graph(...)`, and `feature*PresentationModule`. Depends on own `domain`, `:core:domain`, `:core:presentation`, `:core:designsystem`.

### 3. Shared Compose Resources (`Res`) Strategy
- **Decision**: Host `composeResources` in `:core:designsystem` and configure `compose.resources { publicResClass = true; packageOfResClass = "moneymanagerkmp.composeapp.generated.resources" }`.
- **Rationale**: Over 300 files across `:core` and `:feature:*:presentation` import `moneymanagerkmp.composeapp.generated.resources.*`. Keeping the generated package name avoids 300+ mechanical import rewrites while making `Res` public across all Compose modules.
- **Alternatives considered**: Splitting resources per feature module — rejected due to shared category icons and high churn without functional benefit.

### 4. Koin & Navigation Assembly
- **Decision**:
  - Replace `feature.di.sharedModules` with `coreDataModule`, `homeDataModule` (if needed), `homePresentationModule`, `chartDataModule`, `chartPresentationModule`, `analyticsDataModule`, `analyticsPresentationModule`, and `appModule` (for `AppViewModel`, `CoroutineScope`), assembled in `initKoin(config: KoinAppDeclaration? = null)` inside `:composeApp`.
  - Call `initKoin { androidContext(this@MoneyManagerApp) }` in `:androidApp`'s `MoneyManagerApp.onCreate()`, and `InitKoinKt.doInitKoin()` in `iosApp/iosApp/iOSApp.swift` (removing `startKoin` from `MainViewController.kt`).
  - Move `Route` entries into each feature's `presentation` module (`HomeGraphRoute`, `HomeRoute`, `HomeAddRoute`, `TypesRoute`, `TypeCategoryEditRoute`, `HomeEditRoute`; `ChartGraphRoute`, `ChartRoute`, `ChartDetailRoute`; `AnalyticsGraphRoute`, `AnalyticsRoute`, `BackupRoute`).
  - Pass `onNavigate...` / `NavController` into `homeGraph`, `chartGraph`, and `analyticsGraph` from `:composeApp`'s `App.kt`.

## Risks / Trade-offs

- **Gradle 9 + Realm Kotlin 2.1.0 compatibility** → Test Gradle wrapper upgrade (8.13 vs 9.0) early in task 1; if Realm 2.1.0 fails under Gradle 9, keep Gradle 8.13/8.14 with `com.android.kotlin.multiplatform.library` (which AGP 8.11.2 supports) and document the constraint.
- **Android Preview in KMP modules** → Apply `androidMainImplementation(libs.androidx.compose.ui.tooling)` in `CmpLibraryConventionPlugin` and `CmpApplicationConventionPlugin` per `kmp-module-structure`; move existing `previews/` to their respective modules or `:androidApp` if needed.
- **iOS Xcode build (`ComposeApp.framework`)** → Ensure `:composeApp` still exports `baseName = "ComposeApp"` (`isStatic = true`) and `iosApp` embeds it cleanly after `initKoin` moves to `iOSApp.swift`.
