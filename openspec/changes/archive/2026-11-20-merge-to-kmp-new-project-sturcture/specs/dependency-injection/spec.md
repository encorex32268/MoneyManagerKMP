# Spec Delta

## Purpose

Defines layer-scoped Koin dependency injection modules and unified startup assembly across Android and iOS entry points.

## ADDED Requirements

### Requirement: Layer-scoped Koin modules
Each `data` and `presentation` module with injectable dependencies SHALL expose its own Koin `Module` in `commonMain` (`coreDataModule`, `homeDataModule`, `homePresentationModule`, `chartDataModule`, `chartPresentationModule`, `analyticsDataModule`, `analyticsPresentationModule`).

#### Scenario: Resolving feature dependencies
- **WHEN** a feature screen requests its ViewModel via `koinViewModel()`
- **THEN** the feature's presentation Koin module provides the ViewModel and its repository dependencies are resolved from the corresponding data Koin modules

### Requirement: Centralized Koin initialization in composeApp
The `:composeApp` module SHALL expose an `initKoin` entry point in `commonMain` that registers all core, feature, and app-level Koin modules exactly once.

#### Scenario: Android application startup
- **WHEN** `MoneyManagerApp.onCreate()` executes in `:androidApp`
- **THEN** it calls `initKoin` with `androidContext(this)` and starts Koin without duplicating module registrations

#### Scenario: iOS application startup
- **WHEN** `iOSApp` initializes in `iosApp`
- **THEN** it calls the shared `initKoin` entry point once before rendering `MainViewController`
