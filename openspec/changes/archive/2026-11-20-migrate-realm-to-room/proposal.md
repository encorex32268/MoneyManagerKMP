# Proposal: Migrate Local Persistence from Realm Kotlin to Room KMP

## Why

MoneyManagerKMP currently relies on `io.realm.kotlin`, which locks the project to Kotlin 2.0.10, blocks future Gradle 9 / Compose Multiplatform upgrades, and leaks `org.mongodb.kbson.ObjectId` into `:core:domain`. Migrating local persistence to Room KMP with an automatic one-time Realm-to-Room startup migration unblocks future toolchain upgrades, makes `:core:domain` 100% pure Kotlin, and preserves existing users' local data without requiring manual backup/restore.

## What Changes

- **Pure Kotlin Domain Models (`:core:domain`)**:
  - **BREAKING**: Replace `org.mongodb.kbson.ObjectId` / `BsonObjectId` in `Expense`, `Type`, and `SpendingLimit` with `String` IDs (using `UUID().generateUUID()` or hex string) and remove `io.realm.kotlin:library-base` from `:core:domain`.
- **Room KMP Database & Build Support (`:build-logic`, `:core:database` / `:core:data`)**:
  - Add `RoomConventionPlugin` (`ksp` + `androidx.room`) and Room/SQLite/KSP entries in `gradle/libs.versions.toml` while keeping existing Kotlin/Gradle versions stable for this migration release.
  - Introduce Room `@Database`, `@Entity` tables (`ExpenseRoomEntity`, `TypeRoomEntity`, `CategoryRoomEntity`, `SpendingLimitRoomEntity`), DAOs, and platform `RoomDatabase.Builder` (`expect`/`actual` for Android and iOS using `BundledSQLiteDriver`).
  - Switch `ExpenseRepositoryImpl`, `TypeRepositoryImpl`, `SpendingLimitRepositoryImpl`, `HomeRepositoryImpl`, `ChartRepositoryImpl`, `AnalyticsRepositoryImpl`, and `BackupRepositoryImpl` to Room DAOs.
- **One-Time Realm-to-Room Startup Migration**:
  - Retain `io.realm.kotlin` in read-only capacity for this release (`RealmFactory` + existing Realm entities) and run an idempotent `RealmToRoomMigrator` on startup (gated by a `KeySettings` flag) to copy all `Type`, `Category`, `Expense`, and `SpendingLimit` records into Room.

## Capabilities

### New Capabilities

- `local-database`: Room KMP relational schema, DAOs, platform database builders with bundled SQLite, and Room-backed repository implementations.
- `realm-to-room-migration`: Idempotent one-time startup migration transferring existing Realm records to Room and persisting migration completion state.

### Modified Capabilities

- `module-architecture`: Remove `kbson` / Realm dependencies from `:core:domain` (using `String` IDs) and replace Realm-first persistence in `:core:data` / `:core:database` with Room KMP (retaining Realm read-only for migration).
- `build-system`: Add `RoomConventionPlugin` (`ksp` + `androidx.room`) and Room/SQLite/KSP catalog entries while keeping Kotlin/Gradle versions stable for this release.

## Impact

- **Build & Tooling**: `gradle/libs.versions.toml`, `build.gradle.kts`, `build-logic/convention/` (`RoomConventionPlugin.kt`), `core/database/` (or `core/data/build.gradle.kts`).
- **Domain & Presentation**: `core/domain/model/{Expense,Type,SpendingLimit}.kt`, `NavType`s (`ExpenseNavType`, `ExpenseListNavType`, `TypeNavType`), and any UI/ViewModel code constructing `ObjectId()` directly.
- **Data & DI**: `core/data/` repositories, `BackupRepositoryImpl` CSV export/import (preserving compatibility with existing backups), `coreDataModule`, and Android/iOS startup (`initKoin` / `MoneyManagerApp` / `App`).
