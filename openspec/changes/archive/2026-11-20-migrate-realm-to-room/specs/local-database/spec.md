# Spec Delta

## Purpose

Defines the Room KMP relational local database, DAOs, bundled SQLite setup, and repository persistence behavior replacing Realm for runtime reads and writes.

## ADDED Requirements

### Requirement: Relational Room schema for finance entities
The local database SHALL store `Expense`, `Type`, `Category`, and `SpendingLimit` records in Room tables with `String` primary keys and explicit relational mapping between `Type` and `Category` ordered by `order`.

#### Scenario: Persisting and querying types with categories
- **WHEN** a `Type` containing ordered `Category` items is inserted or updated
- **THEN** Room persists the `Type` and its associated `Category` rows and queries return the `Type` with its categories in order

### Requirement: Cross-platform Room database builder
The data layer SHALL construct the Room database using `BundledSQLiteDriver` and platform-appropriate database file paths on both Android and iOS.

#### Scenario: Database initialization on Android and iOS
- **WHEN** the database singleton is resolved via DI on Android or iOS
- **THEN** Room opens the SQLite database file with `BundledSQLiteDriver` and supports reactive `Flow` queries and suspend writes

### Requirement: Room-backed repository operations
All core and feature repositories (`Expense`, `Type`, `SpendingLimit`, `Home`, `Chart`, `Analytics`, `Backup`) SHALL read from and write to Room DAOs for all runtime operations.

#### Scenario: Creating, updating, and deleting expenses
- **WHEN** a user adds, edits, or deletes an expense
- **THEN** the change is persisted in Room and reactive UI streams emit the updated list immediately
