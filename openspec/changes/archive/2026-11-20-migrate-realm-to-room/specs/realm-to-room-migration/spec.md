# Spec Delta

## Purpose

Defines the idempotent one-time startup migration that transfers existing Realm data into Room KMP so existing users retain their records across app updates.

## ADDED Requirements

### Requirement: One-time automatic Realm-to-Room migration
On application startup, if the migration completion flag is not set, the system SHALL open the existing Realm database (applying any pending Realm schema migration `0 -> 1`), read all `Type`, `Category`, `Expense`, and `SpendingLimit` records, and insert them into Room inside a transaction.

#### Scenario: Existing user upgrades to Room release
- **WHEN** an existing user with Realm data launches the updated app for the first time
- **THEN** all existing expenses, types, categories, and spending limits are copied into Room preserving IDs (converted to hex `String`), timestamps, amounts, and ordering, and the migration flag is marked complete

#### Scenario: Fresh install with no prior Realm data
- **WHEN** a new user launches the app with no existing Realm records
- **THEN** the migrator finds zero records to copy, marks migration complete without error, and normal app initialization proceeds

### Requirement: Idempotent migration execution
The migration SHALL be idempotent so that subsequent app launches skip Realm migration once completed, and a partial failure does not mark migration complete or duplicate records on retry.

#### Scenario: Subsequent app launches after migration
- **WHEN** the user launches the app after migration has completed
- **THEN** the migration step is skipped immediately without opening Realm

#### Scenario: Retry after interrupted migration
- **WHEN** migration fails before committing the Room transaction
- **THEN** the completion flag remains unset and the next startup retries cleanly using `Upsert` / `REPLACE` without duplicating records
