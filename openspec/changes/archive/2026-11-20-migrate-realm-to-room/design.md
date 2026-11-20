# Design: Migrate Local Persistence from Realm Kotlin to Room KMP

## Context

MoneyManagerKMP currently uses MongoDB Realm (`io.realm.kotlin:2.1.0` / `2.3.0`) in `:core:data` and leaks `org.mongodb.kbson.ObjectId` into `:core:domain` models (`Expense`, `Type`, `SpendingLimit`). This locks the project to Kotlin `2.0.10` and violates pure-Kotlin `:core:domain` boundaries (see `proposal.md`).

## Goals / Non-Goals

**Goals:**
- Replace Realm with Room KMP + `BundledSQLiteDriver` across Android and iOS for all runtime reads/writes.
- Convert `:core:domain` entity IDs from `ObjectId` to `String` so `:core:domain` has zero database library dependencies.
- Provide an idempotent one-time startup migration (`RealmToRoomMigrator`) that copies existing Realm records into Room on upgrade without data loss.
- Add `RoomConventionPlugin` (`ksp` + `androidx.room`) to `:build-logic` per `kmp-module-structure` and `kmp-data-layer`.

**Non-Goals:**
- Upgrading Kotlin (`2.0.10`), Compose Multiplatform (`1.7.0`), or Gradle (`8.13`) in this change (deferred to a follow-up once Realm is removed in the next major release).
- Removing `io.realm.kotlin` dependencies yet (retained read-only for one transition release to support existing user migration).

## Decisions

### 1. Pure Kotlin `String` IDs in `:core:domain`
- **Decision**: Change `Expense.id`, `Type.id`, and `SpendingLimit.id` from `org.mongodb.kbson.ObjectId` to `String` (defaulting to `UUID().generateUUID()`), and remove `api(libs.library.base)` from `core/domain/build.gradle.kts`.
- **Rationale**: Decouples `:core:domain` from `kbson` and makes `@Serializable` nav routes (`HomeAdd`, `HomeEdit`, `TypeCategoryEdit`, `ChartDetail`) simpler without custom `ObjectId` serialization.
- **Alternatives considered**: Keeping `ObjectId` in `:core:domain` — rejected because `kbson` is a Realm/MongoDB artifact that blocks eventual Realm removal.

### 2. Room KMP Schema & Hosting Module
- **Decision**: Host Room `@Database`, `@Entity` tables, DAOs, and platform `RoomDatabase.Builder` (`expect`/`actual` with `BundledSQLiteDriver`) in `:core:data` (applying `convention.room`), keeping all Realm entities and `RealmFactory` isolated in `core.data.realm` for migration only.
- **Schema Mapping**:
  - `ExpenseRoomEntity` (`@PrimaryKey val id: String`, `categoryId`, `typeId`, `description`, `isIncome`, `cost`, `timestamp`, `content`)
  - `TypeRoomEntity` (`@PrimaryKey val id: String`, `typeIdHex`, `typeIdTimestamp`, `name`, `colorArgb`, `order`, `isShow`)
  - `CategoryRoomEntity` (`@PrimaryKey(autoGenerate = true) val rowId: Long = 0`, `id: Int`, `name: String`, `order: Int`, `typeId: Long`, `parentTypeId: String` with `@ForeignKey` referencing `TypeRoomEntity(id)` `onDelete = CASCADE`)
  - `SpendingLimitRoomEntity` (`@PrimaryKey val id: String`, `year`, `month`, `limit`)
- **Rationale**: `TypeEntity` previously embedded `RealmList<CategoryEntity>`; normalizing `CategoryRoomEntity` with a foreign key to `TypeRoomEntity` models the 1-to-many relationship cleanly in SQLite and supports `@Transaction` queries with `@Relation`.
- **Alternatives considered**: Creating a separate `:core:database` module — keeping it in `:core:data` (or extracting `:core:database` later) avoids extra module churn while still isolating Room behind `core:domain` repository interfaces.

### 3. One-Time Startup Migration (`RealmToRoomMigrator`)
- **Decision**: Add `isRealmMigrated` boolean flag in `KeySettings`. On startup (inside `AppViewModel` / `CoreDataInitializer` before main data flows are collected, or guarded by a `CompletableDeferred` / `StateFlow<Boolean>`), if `!keySettings.isRealmMigrated()`:
  1. Open Realm via `RealmFactory().build()` (running `migration_0_to_1` if needed).
  2. Read all `TypeEntity`, `ExpenseEntity`, and `SpendingLimitEntity` objects.
  3. Map `ObjectId.toHexString()` to Room `String` IDs and upsert all records inside `database.useWriterConnection { ... }` / `@Transaction` DAO calls.
  4. Close Realm and set `keySettings.setRealmMigrated(true)`.
- **Rationale**: Opening Realm only when `!isRealmMigrated()` avoids runtime overhead on subsequent launches, and transactional upserts guarantee idempotency if interrupted.

### 4. KSP & Room Version Selection
- **Decision**: Use KSP `2.0.10-1.0.24` (matching `kotlin = "2.0.10"`) and Room KMP `2.7.0-alpha11` / SQLite `2.5.0-alpha11` (or latest 2.7.x compatible with Kotlin 2.0.10) in `libs.versions.toml`.

## Risks / Trade-offs

- **Kotlin 2.0.10 + Room KMP / KSP compatibility** → Verify KSP `2.0.10-1.0.24` and Room `2.7.x` compile on both Android and iOS (`iosSimulatorArm64`) in Task 1 before rewriting repositories.
- **CSV Backup/Restore compatibility** → Ensure `BackupRepositoryImpl` preserves the existing CSV column format so backups exported from the Realm version can still be restored into the Room version.
- **Startup race condition** → Gate UI / ViewModel initial queries or run migration synchronously/suspending in `AppViewModel` before displaying `HomeScreen` so users never briefly see an empty screen before migration finishes.
