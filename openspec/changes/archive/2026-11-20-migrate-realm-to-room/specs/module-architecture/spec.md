# Spec Delta

## MODIFIED Requirements

### Requirement: Strict unidirectional dependency boundaries
Each module SHALL obey clean architecture dependency rules: `domain` modules depend only on `:core:domain`; `data` modules depend on their own `domain`, `:core:domain`, and `:core:data`; `presentation` modules depend on their own `domain`, `:core:domain`, `:core:presentation`, and `:core:designsystem`; feature modules NEVER depend on other feature modules.

#### Scenario: Isolating domain from data and UI
- **WHEN** `:core:domain` or any `:feature:*:domain` module is compiled
- **THEN** it has zero dependencies on database libraries (`kbson`, Realm, Room), Compose UI, or presentation models, and uses pure Kotlin `String` IDs

#### Scenario: Preventing cross-feature coupling
- **WHEN** `:feature:home:*`, `:feature:chart:*`, or `:feature:analytics:*` modules are compiled
- **THEN** no feature module declares a Gradle dependency on another feature module

### Requirement: Realm persistence compatibility
The `:core:data` module SHALL retain the existing MongoDB Realm schema entities, `RealmFactory`, and `RealmMigration` in a read-only migration capacity for this transition release so existing local user data is automatically migrated to Room on startup.

#### Scenario: Opening existing database after update
- **WHEN** the updated app launches on a device with existing Realm data
- **THEN** expenses, categories, types, and spending limits are migrated to Room without data loss or schema mismatch
