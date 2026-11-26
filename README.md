<div align="center">

# Qash — Kotlin Multiplatform Expense Tracker

**A production cross-platform personal finance app built with Kotlin Multiplatform & Compose Multiplatform, shipped on Google Play and the App Store.**

[![Kotlin](https://img.shields.io/badge/Kotlin-2.0.10-7F52FF?logo=kotlin&logoColor=white)](https://kotlinlang.org)
[![Compose Multiplatform](https://img.shields.io/badge/Compose%20Multiplatform-1.7.0-4285F4?logo=jetpackcompose&logoColor=white)](https://www.jetbrains.com/lp/compose-multiplatform/)
[![Android](https://img.shields.io/badge/Android-API%2024%E2%80%9336-3DDC84?logo=android&logoColor=white)](https://play.google.com/store/apps/details?id=com.lihan.moneymanager)
[![iOS](https://img.shields.io/badge/iOS-SwiftUI%20Host-000000?logo=apple&logoColor=white)](https://apps.apple.com/us/app/moneymanager-expensetracker/id6503697767)
[![Architecture](https://img.shields.io/badge/Architecture-Clean%20%2B%20MVI-0A66C2)](#architecture--modularization)
[![CI/CD](https://img.shields.io/badge/CI%2FCD-Bitrise-683D87?logo=bitrise&logoColor=white)](bitrise.yml)

<br/>

[![Download on the App Store](https://img.shields.io/badge/App%20Store-Qash%20(iOS)-0D96F6?style=for-the-badge&logo=apple&logoColor=white)](https://apps.apple.com/us/app/moneymanager-expensetracker/id6503697767)
&nbsp;
[![Get it on Google Play](https://img.shields.io/badge/Google%20Play-Qash%20(Android)-34A853?style=for-the-badge&logo=googleplay&logoColor=white)](https://play.google.com/store/apps/details?id=com.lihan.moneymanager)

</div>

---

## Overview

**Qash** (`MoneyManagerKMP`) is an offline-first expense tracking application sharing UI, presentation, domain, and data layers across **Android** and **iOS** via **Kotlin Multiplatform (KMP)** and **Compose Multiplatform**.

Built with a focus on real-world production engineering, the project demonstrates:
- **15-module Clean Architecture** with strict layer boundaries and custom Gradle convention plugins (`build-logic`).
- **Unidirectional Data Flow (MVI)** across all screens with immutable state, sealed actions, and one-shot UI events.
- **Zero-data-loss database migration** from Realm Kotlin to **Room KMP** with bundled SQLite.
- **Custom Design System (`MoneyTheme`)** supporting Light/Dark modes and 3 locales (English, Traditional Chinese, Japanese).
- **Automated unit testing** (`commonTest`) and **Bitrise CI/CD** pipelines for PR validation and release distribution.

---

## Screenshots

> Shared **Compose Multiplatform** UI running natively on both **Android** (Pixel 10 Pro) and **iOS** (iPhone 18 Pro), with full Light & Dark theme support.

### Android — Light Mode

| Home & Budget Ring | Category Breakdown | Trend Analytics | Quick Add & Keypad |
| :---: | :---: | :---: | :---: |
| <img src="screenshots/android/home_light.png" width="210" alt="Android Home screen in light mode"/> | <img src="screenshots/android/chart_light.png" width="210" alt="Android Category chart in light mode"/> | <img src="screenshots/android/analytics_light.png" width="210" alt="Android Trend analytics in light mode"/> | <img src="screenshots/android/add_expense_light.png" width="210" alt="Android Add expense keypad in light mode"/> |

### Android — Dark Mode

| Home & Budget Ring | Category Breakdown | Trend Analytics | Quick Add & Keypad |
| :---: | :---: | :---: | :---: |
| <img src="screenshots/android/home_dark.png" width="210" alt="Android Home screen in dark mode"/> | <img src="screenshots/android/chart_dark.png" width="210" alt="Android Category chart in dark mode"/> | <img src="screenshots/android/analytics_dark.png" width="210" alt="Android Trend analytics in dark mode"/> | <img src="screenshots/android/add_expense_dark.png" width="210" alt="Android Add expense keypad in dark mode"/> |

### iOS — Light Mode

| Home & Budget Ring | Category Breakdown | Trend Analytics | Quick Add |
| :---: | :---: | :---: | :---: |
| <img src="screenshots/ios/home_light.png" width="210" alt="iOS Home screen in light mode"/> | <img src="screenshots/ios/chart_light.png" width="210" alt="iOS Category chart in light mode"/> | <img src="screenshots/ios/analytics_light.png" width="210" alt="iOS Trend analytics in light mode"/> | <img src="screenshots/ios/add_expense_light.png" width="210" alt="iOS Add expense screen in light mode"/> |

### iOS — Dark Mode

| Home & Budget Ring | Category Breakdown | Trend Analytics | Quick Add |
| :---: | :---: | :---: | :---: |
| <img src="screenshots/ios/home_dark.png" width="210" alt="iOS Home screen in dark mode"/> | <img src="screenshots/ios/chart_dark.png" width="210" alt="iOS Category chart in dark mode"/> | <img src="screenshots/ios/analytics_dark.png" width="210" alt="iOS Trend analytics in dark mode"/> | <img src="screenshots/ios/add_expense_dark.png" width="210" alt="iOS Add expense screen in dark mode"/> |

---

## Key Features

- **Monthly Budget & Spending Ring**: Set monthly spending limits with customizable warning thresholds and real-time remaining budget tracking.
- **Fast Expense Entry**: Built-in numeric keypad, date picker, and "Recently Used" category shortcuts for one-handed logging.
- **Flexible Category Management**: Create, edit, color-code, hide/show, and drag-and-drop reorder expense types and subcategories.
- **Visual Breakdown & Drill-Down**: Interactive donut chart grouping monthly spending by category with percentage shares and transaction drill-down.
- **Multi-Range Trend Analytics**: Line chart visualizing spending trends and daily averages across `7D`, `1M`, `3M`, `6M`, `1Y`, and `All` timeframes.
- **Local Backup & Restore**: Export and import expense records offline using native file pickers (`FileKit`) and CSV/JSON serialization.
- **Theming & Localization**: Instant in-app Light/Dark theme switching and full localization in **English**, **繁體中文**, and **日本語**.

---

## Architecture & Modularization

The codebase follows **Clean Architecture** combined with **Feature-Sliced Modularization** (`15` Gradle modules) and Gradle's `TYPESAFE_PROJECT_ACCESSORS`.

```mermaid
flowchart TD
    AndroidApp[":androidApp<br/>Android Entry & Splash"] --> ComposeApp
    iOSApp["iosApp<br/>SwiftUI Host"] --> ComposeApp
    ComposeApp[":composeApp<br/>App Root, Navigation, Koin Wiring"]

    ComposeApp --> HomeP[":feature:home:presentation"]
    ComposeApp --> ChartP[":feature:chart:presentation"]
    ComposeApp --> AnalyticsP[":feature:analytics:presentation"]

    HomeP --> HomeD[":feature:home:domain"]
    HomeData[":feature:home:data"] --> HomeD

    ChartP --> ChartD[":feature:chart:domain"]
    ChartData[":feature:chart:data"] --> ChartD

    AnalyticsP --> AnalyticsD[":feature:analytics:domain"]
    AnalyticsData[":feature:analytics:data"] --> AnalyticsD

    ComposeApp --> HomeData
    ComposeApp --> ChartData
    ComposeApp --> AnalyticsData

    HomeP & ChartP & AnalyticsP --> CoreP[":core:presentation"]
    HomeP & ChartP & AnalyticsP --> CoreDS[":core:designsystem"]
    HomeD & ChartD & AnalyticsD --> CoreDom[":core:domain"]
    HomeData & ChartData & AnalyticsData --> CoreData[":core:data"]
    CoreData --> CoreDom
```

### Module Breakdown

| Module | Responsibility |
| --- | --- |
| `:androidApp` | Android `Application`, `MainActivity`, Splash Screen, and Android-specific wiring |
| `iosApp` | Thin SwiftUI wrapper hosting `MainViewController` from `:composeApp` |
| `:composeApp` | Shared Compose entry point, root navigation graph, and Koin DI assembly (`initKoin`) |
| `:core:domain` | Pure Kotlin domain models, repository interfaces, and formatting/date utilities (zero UI/Android deps) |
| `:core:data` | Room KMP database (`MoneyManagerDatabase`), DAOs, entities, mappers, `MultiplatformSettings`, and Realm→Room migrator |
| `:core:designsystem` | `MoneyTheme` tokens, `Plus Jakarta Sans` typography, vector icons, shared components, and Compose Multiplatform resources |
| `:core:presentation` | Shared presentation utilities (`UiText`, platform helpers, reusable UI patterns) |
| `:feature:home:{domain,data,presentation}` | Monthly expense feed, spending limit progress & warnings, add/edit expense keypad, drag-to-reorder category editor |
| `:feature:chart:{domain,data,presentation}` | Monthly category donut chart and per-category transaction detail view |
| `:feature:analytics:{domain,data,presentation}` | Multi-range trend line chart, theme toggle, and offline CSV/JSON backup & restore |
| `build-logic` | 8 custom Gradle convention plugins standardizing KMP, CMP, Feature, Room, and BuildKonfig setup |

---

## Engineering Highlights

### 1. MVI Unidirectional Data Flow
Every screen separates state management from rendering via a **Root / Screen composable split**:
- `ViewModel` exposes a single immutable `StateFlow<State>`, handles `onAction(Action)`, and emits one-shot side effects (`UiEvent`) over a `Channel`.
- Stateless `Screen` composables accept `State` and `(Action) -> Unit`, keeping UI previews clean and ViewModel logic 100% unit-testable in `commonTest`.

### 2. Zero-Data-Loss Realm → Room KMP Migration
Originally built on Realm Kotlin, Qash was migrated to **Room KMP** (`androidx.room` + `androidx.sqlite:sqlite-bundled`) for long-term KMP stability and SQL performance:
- Implemented an automated, idempotent `RealmToRoomMigrator` that detects legacy Realm data on startup and migrates expenses, types, categories, and spending limits into Room without disrupting existing App Store / Google Play users.
- Versioned exported Room schemas under `core/data/schemas/`.

### 3. Build Scalability with Convention Plugins
All 15 modules share a single Version Catalog (`gradle/libs.versions.toml`) and 8 convention plugins in `build-logic/`:
- `convention.kmp.library`, `convention.cmp.library`, `convention.cmp.feature`, `convention.cmp.application`
- `convention.android.application`, `convention.android.application.compose`, `convention.room`, `convention.buildkonfig`

### 4. Multiplatform Testing & CI/CD
- **Unit Tests**: **17 `commonTest` test suites (53 tests)** across `:core:domain`, `:core:data`, and all three `:feature` modules covering ViewModels, repositories, mappers, and date/number formatters using `kotlin.test` and `kotlinx-coroutines-test` with in-memory fakes.
- **Bitrise CI/CD** (`bitrise.yml`):
  - `ci_test_and_build` (on Pull Requests): Runs all KMP & Android unit tests (`testAndroidHostTest testDebugUnitTest`) and builds the Debug APK on JDK 21.
  - `deploy_android` (on `master` / `main`): Runs the full test suite, builds & signs the Release AAB, and supports automated deployment to Firebase App Distribution and Google Play Internal Track.

---

## Tech Stack

| Category | Technologies |
| --- | --- |
| **Language & Multiplatform** | Kotlin `2.0.10`, Kotlin Multiplatform (Android + iOS), Coroutines & Flow |
| **UI & Design System** | Compose Multiplatform `1.7.0`, Material 3, Custom `MoneyTheme`, Custom Compose `Canvas` Charts, `colorpicker-compose` |
| **Architecture & Navigation** | Clean Architecture, MVI, Type-Safe Navigation Compose (`navigation-compose`) |
| **Dependency Injection** | Koin (`koin-core`, `koin-compose`, `koin-compose-viewmodel`) |
| **Local Persistence** | Room KMP (`2.7.0-alpha11`), Bundled SQLite (`sqlite-bundled`), `multiplatform-settings` |
| **Data & Files** | `kotlinx.serialization`, `kotlinx-datetime`, `FileKit`, `de.halfbit:csv` |
| **Build & CI/CD** | Gradle Convention Plugins (`build-logic`), Version Catalogs, KSP, BuildKonfig, Bitrise |
| **Testing** | `kotlin.test`, `kotlinx-coroutines-test`, Fake Repositories in `commonTest` |

---

## Getting Started

### Prerequisites
- **JDK 21** (required by Gradle & AGP)
- **Android Studio** (Ladybug or newer) for Android & KMP development
- **Xcode** (macOS) to build and run the iOS app

### Build & Run

```bash
# 1. Clone the repository
git clone https://github.com/encorex32268/MoneyManagerKMP.git
cd MoneyManagerKMP

# 2. Run all KMP host unit tests
./gradlew testAndroidHostTest --console=plain

# 3. Build Android Debug APK
./gradlew :androidApp:assembleDebug

# 4. Install & run on a connected Android device/emulator
./gradlew :androidApp:installDebug
```

> **iOS**: Open `iosApp/iosApp.xcodeproj` in Xcode, select a simulator or device, and run.
>
> **Configuration**: No API keys are required to build locally — `BuildKonfig` safely defaults to empty strings if `local.properties` has no custom keys.

---

## License

This project is developed and maintained by [Li-Han](https://github.com/encorex32268) as a production cross-platform mobile application and engineering portfolio project.
