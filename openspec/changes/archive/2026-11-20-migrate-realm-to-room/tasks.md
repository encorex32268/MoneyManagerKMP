# Tasks

## 1. Room Convention Plugin & Version Catalog

- [x] 1.1 Add KSP (`2.0.10-1.0.24`), Room (`androidx.room`), and SQLite (`androidx.sqlite:sqlite-bundled`) versions, libraries, and plugins (`ksp`, `room`, `convention-room`) to `gradle/libs.versions.toml` and root `build.gradle.kts`; verify `libs.versions.toml` parses cleanly.
- [x] 1.2 Implement `RoomConventionPlugin` in `:build-logic:convention` (applying `com.google.devtools.ksp` + `androidx.room`, configuring `RoomExtension.schemaDirectory`, and adding `androidx-room-runtime`, `sqlite-bundled`, and `kspAndroid` / `kspIos*` dependencies); verify `./gradlew -p build-logic :convention:check` succeeds.

## 2. Pure Kotlin `:core:domain` ID Refactoring

- [x] 2.1 Replace `org.mongodb.kbson.ObjectId` / `BsonObjectId` with `String` (defaulting to `UUID().generateUUID()`) in `Expense`, `Type`, and `SpendingLimit`, remove `api(libs.library.base)` from `core/domain/build.gradle.kts`, and add `IS_REALM_MIGRATED` methods to `KeySettings`; verify `:core:domain` compiles for Android and iOS.
- [x] 2.2 Update any call sites in `:core:presentation`, `:feature:home:*`, `:feature:chart:*`, and `:feature:analytics:*` that construct `ObjectId()` or serialize `ObjectId` in navigation/mappers to use `String` IDs; verify all presentation and domain modules compile for Android and iOS.

## 3. Room KMP Database & DAOs in `:core:data`

- [x] 3.1 Apply `convention.room` to `core/data/build.gradle.kts` and create `ExpenseRoomEntity`, `TypeRoomEntity`, `CategoryRoomEntity` (with foreign key to `TypeRoomEntity`), `SpendingLimitRoomEntity`, `TypeWithCategories` relation, and `ExpenseDao`, `TypeDao`, `SpendingLimitDao`; verify KSP generates Room implementations for Android and iOS.
- [x] 3.2 Create `MoneyManagerDatabase` (`@Database`) and platform-specific `expect`/`actual` database builder (`androidMain` using `Context.getDatabasePath` and `iosMain` using `NSDocumentDirectory`) configured with `BundledSQLiteDriver`, plus Room↔Domain mappers and `DefaultKeySettings` migration flag; verify `:core:data` compiles for Android and iOS.

## 4. Repository Migration & One-Time Realm-to-Room Migrator

- [x] 4.1 Switch `ExpenseRepositoryImpl`, `TypeRepositoryImpl`, `SpendingLimitRepositoryImpl`, `ChartRepositoryImpl`, `AnalyticsRepositoryImpl`, and `BackupRepositoryImpl` to Room DAOs (preserving CSV backup/restore compatibility), and update `coreDataModule` in Koin; verify `:core:data` and `:feature:*:data` compile for Android and iOS.
- [x] 4.2 Implement `RealmToRoomMigrator` in `:core:data` (reading `TypeEntity`, `ExpenseEntity`, `SpendingLimitEntity` from `RealmFactory` when `!keySettings.isRealmMigrated()`, converting `ObjectId.toHexString()` to `String`, upserting into Room inside a transaction, closing Realm, and setting `isRealmMigrated = true`) and wire startup execution in `AppViewModel` / `App.kt` before main content renders; verify `:composeApp:compileAndroidMain` and `:composeApp:linkDebugFrameworkIosSimulatorArm64`.
- [x] 4.3 Add unit tests in `core/data/src/commonTest` (using in-memory / fake DAOs or Room in-memory builder) covering `RealmToRoomMigrator` idempotency, `TypeWithCategories` ordering, and `ExpenseDao` CRUD; verify `testAndroidHostTest` passes.

## 5. End-to-End Verification

- [x] 5.1 Run `./gradlew :androidApp:assembleDebug testAndroidHostTest iosSimulatorArm64Test` and verify all build and test tasks pass.
- [x] 5.2 Install and launch `:androidApp` on emulator/device, verify startup migration completes cleanly, confirm creating/editing/deleting expenses, categories, types, and spending limits persist across app restarts, and verify CSV backup/restore works.
