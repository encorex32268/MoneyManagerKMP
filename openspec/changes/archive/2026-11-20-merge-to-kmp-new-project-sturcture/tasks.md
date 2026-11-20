# Tasks

## 1. Build-Logic & Version Catalog Setup

- [x] 1.1 Update `gradle/libs.versions.toml` with project config versions (`projectApplicationId`, `projectCompileSdkVersion`, `projectMinSdkVersion`, `projectTargetSdkVersion`, `projectVersionCode`, `projectVersionName`), Gradle plugin library artifacts (`*-gradlePlugin`), `android-kotlin-multiplatform-library`, `androidx-compose-ui-tooling`, and convention plugin aliases (`convention-*`); verify `libs.versions.toml` parses cleanly.
- [x] 1.2 Create `:build-logic` (`build-logic/settings.gradle.kts`, `build-logic/gradle.properties`, `build-logic/convention/build.gradle.kts`) and implement helpers (`ProjectExt.kt`, `PathUtil.kt`, `KotlinAndroid.kt`, `AndroidCompose.kt`, `KotlinMultiplatform.kt` without desktop target, `KotlinIosTargets.kt`) and convention plugins (`AndroidApplicationConventionPlugin`, `AndroidApplicationComposeConventionPlugin`, `KmpLibraryConventionPlugin`, `CmpLibraryConventionPlugin`, `CmpFeatureConventionPlugin`, `CmpApplicationConventionPlugin`, `BuildKonfigConventionPlugin`); verify `./gradlew :build-logic:convention:check` succeeds.
- [x] 1.3 Update root `build.gradle.kts`, `settings.gradle.kts` (adding `includeBuild("build-logic")` and all `:androidApp`, `:core:*`, `:feature:*`, `:composeApp` includes), and `gradle.properties`; verify Gradle sync / `:help` succeeds without configuration errors.

## 2. Core Modules Extraction

- [x] 2.1 Create `:core:domain` (`core/domain/build.gradle.kts` with `convention.kmp.library`), move domain models (`Category`, `Expense`, `SpendingLimit`, `Type`, `Chart`), repository interfaces, `KeySettings`, `CategoryData`, `exts/`, `ZeroStringDisplay`, `FileLogger`, and pure platform `expect`/`actual` utilities (`AppLogger`, `UUID`, `Formatter`, `GetDownloadsPath`), removing any Entity/Compose/UI imports; verify `:core:domain` compiles for Android and iOS.
- [x] 2.2 Create `:core:data` (`core/data/build.gradle.kts` with `convention.kmp.library` + `io.realm.kotlin`), move Realm entities, `RealmFactory`, `RealmMigration`, `MongoDB`, `DefaultKeySettings`, core repository implementations, Entity↔Domain mappers (extracted from `core.domain.mapper`), and `coreDataModule` (Koin); verify `:core:data` compiles for Android and iOS.
- [x] 2.3 Create `:core:designsystem` (`core/designsystem/build.gradle.kts` with `convention.cmp.library` + `convention.buildkonfig`), move `Color.kt`, `AppTypography.kt`, `composeResources` (configured with `publicResClass = true` and `packageOfResClass = "moneymanagerkmp.composeapp.generated.resources"`), reusable components (`CircleIcon`, `DatePicker`, `DeleteDialog`, `OneButtonDialog`, `SpendingLimitsProgressBar`), and `AdMobBanner` (`expect`/`actual` using `BuildKonfig`); verify `:core:designsystem` compiles for Android and iOS.
- [x] 2.4 Create `:core:presentation` (`core/presentation/build.gradle.kts` with `convention.cmp.library`), move `ObserveAsEvents`, `UiText`, `ModifierExt`, `DateConverter`, `CategoryList`, `CategoryUi` + UI↔Domain mappers, `GetScreenSize` (`expect`/`actual`), `NavigationLayoutType`, `AppNavigationBottom`/`AppNavigationRail` (decoupled from feature `Route`), and shared `NavType`s; verify `:core:presentation` compiles for Android and iOS.

## 3. Feature Modules Extraction

- [x] 3.1 Create `:feature:home:domain`, `:feature:home:data`, and `:feature:home:presentation`, move Home repository/utils, ViewModels, Screens, `ExpenseUi` + mapper, `@Serializable` Home routes, `NavGraphBuilder.homeGraph(...)`, and `homePresentationModule` (`homeDataModule` if needed); verify all `:feature:home:*` modules compile for Android and iOS.
- [x] 3.2 Create `:feature:chart:domain`, `:feature:chart:data`, and `:feature:chart:presentation`, move `ChartRepository`, `ChartRepositoryImpl`, ViewModels, Screens, `@Serializable` Chart routes, `NavGraphBuilder.chartGraph(...)`, `chartDataModule`, and `chartPresentationModule`; verify all `:feature:chart:*` modules compile for Android and iOS.
- [x] 3.3 Create `:feature:analytics:domain`, `:feature:analytics:data`, and `:feature:analytics:presentation`, move `AnalyticsRepository`, `BackupRepository`, `BackupData`, `BackupResult`, data implementations, ViewModels, Screens, `@Serializable` Analytics routes, `NavGraphBuilder.analyticsGraph(...)`, `analyticsDataModule`, and `analyticsPresentationModule`; verify all `:feature:analytics:*` modules compile for Android and iOS.

## 4. Umbrella `:composeApp`, `:androidApp`, and `iosApp` Wiring

- [x] 4.1 Refactor `:composeApp` (`composeApp/build.gradle.kts` with `convention.cmp.application`) to hold `App.kt`, `AppTheme.kt`, `AppViewModel.kt`, `AppState.kt`, `AppEvent.kt`, `initKoin()`, `MainViewController.kt` (without `startKoin`), and `NavHost` wiring `homeGraph`, `chartGraph`, and `analyticsGraph`; remove `desktopMain` remnants and verify `:composeApp` compiles for Android and iOS (`linkDebugFrameworkIosSimulatorArm64`).
- [x] 4.2 Create `:androidApp` (`androidApp/build.gradle.kts` with `convention.android.application.compose`), move `MainActivity.kt`, `MoneyManagerApp.kt` (calling `initKoin { androidContext(this@MoneyManagerApp) }`), `AndroidManifest.xml`, Android `res/`, and AdMob `resValue`s; verify `:androidApp:assembleDebug` succeeds.
- [x] 4.3 Update `iosApp/iosApp/iOSApp.swift` to call `InitKoinKt.doInitKoin()` in `init()`, and verify `xcodebuild` (or iOS framework link + simulator build) succeeds.

## 5. End-to-End Verification

- [x] 5.1 Run full Gradle build and unit tests (`./gradlew :androidApp:assembleDebug testAndroidHostTest` / `iosSimulatorArm64Test`) and verify all tasks pass with zero errors.
- [x] 5.2 Launch/verify the Android app (and iOS app if simulator is available) to confirm startup, Koin DI resolution, Realm data loading, tab navigation (Home, Chart, Analytics), and sub-screen navigation work normally.
