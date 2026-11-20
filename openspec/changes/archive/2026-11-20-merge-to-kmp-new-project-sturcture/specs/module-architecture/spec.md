# Spec Delta

## Purpose

Defines the feature-layered KMP module layout, dependency boundaries, shared Compose resources, and platform-specific `expect`/`actual` organization.

## ADDED Requirements

### Requirement: Core and feature module layout
The project SHALL organize shared code into `:core:domain`, `:core:data`, `:core:presentation`, and `:core:designsystem`, and feature code into `:feature:<name>:domain`, `:feature:<name>:data`, and `:feature:<name>:presentation` for `home`, `chart`, and `analytics`.

#### Scenario: Locating feature and core responsibilities
- **WHEN** a developer inspects any feature (`home`, `chart`, `analytics`) or shared concern
- **THEN** domain models and repository interfaces reside in `domain`, Realm/settings/file implementations and entity mappers reside in `data`, and ViewModels, screens, UI models, and feature routes reside in `presentation`

### Requirement: Strict unidirectional dependency boundaries
Each module SHALL obey clean architecture dependency rules: `domain` modules depend only on `:core:domain`; `data` modules depend on their own `domain`, `:core:domain`, and `:core:data`; `presentation` modules depend on their own `domain`, `:core:domain`, `:core:presentation`, and `:core:designsystem`; feature modules NEVER depend on other feature modules.

#### Scenario: Isolating domain from data and UI
- **WHEN** `:core:domain` or any `:feature:*:domain` module is compiled
- **THEN** it has zero dependencies on Realm entities, Compose UI, or presentation models

#### Scenario: Preventing cross-feature coupling
- **WHEN** `:feature:home:*`, `:feature:chart:*`, or `:feature:analytics:*` modules are compiled
- **THEN** no feature module declares a Gradle dependency on another feature module

### Requirement: Shared Compose resources accessibility
All modules containing Compose UI (`:core:designsystem`, `:core:presentation`, `:feature:*:presentation`, `:composeApp`) SHALL resolve shared drawable, font, and string resources consistently without duplicating resource files.

#### Scenario: Rendering localized strings and icons across modules
- **WHEN** any feature presentation screen or core component references a shared `Res` drawable, font, or string
- **THEN** it compiles and renders the localized resource on both Android and iOS at runtime

### Requirement: Realm persistence compatibility
The `:core:data` module SHALL retain the existing MongoDB Realm schema entities, `RealmFactory`, and `RealmMigration` so existing local user data remains readable after restructuring.

#### Scenario: Opening existing database after update
- **WHEN** the restructured app launches on a device with existing Realm data
- **THEN** expenses, categories, types, and spending limits load without data loss or schema mismatch
