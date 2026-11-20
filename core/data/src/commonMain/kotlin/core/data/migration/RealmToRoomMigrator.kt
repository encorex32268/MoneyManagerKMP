package core.data.migration

import AppLogger
import core.data.RealmFactory
import core.data.database.MoneyManagerDatabase
import core.data.database.mapper.toRoomEntity
import core.data.database.mapper.toRoomTypeAndCategories
import core.data.model.ExpenseEntity
import core.data.model.SpendingLimitEntity
import core.data.model.TypeEntity
import core.domain.KeySettings
import core.domain.mapper.toExpense
import core.domain.mapper.toSpendingLimit
import core.domain.mapper.toType
import io.realm.kotlin.ext.query
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import kotlinx.coroutines.withContext

class RealmToRoomMigrator(
    private val realmFactory: RealmFactory,
    private val database: MoneyManagerDatabase,
    private val keySettings: KeySettings
) {
    private val mutex = Mutex()

    suspend fun migrateIfNeeded() {
        if (keySettings.isRealmMigrated()) return

        mutex.withLock {
            if (keySettings.isRealmMigrated()) return

            withContext(Dispatchers.IO) {
                val realm = realmFactory.build()
                try {
                    val types = realm.query<TypeEntity>().find().map { it.toType() }
                    val expenses = realm.query<ExpenseEntity>().find().map { it.toExpense() }
                    val spendingLimits = realm.query<SpendingLimitEntity>().find().map { it.toSpendingLimit() }

                    if (types.isNotEmpty()) {
                        database.typeDao().upsertAllTypesWithCategories(
                            types.map { it.toRoomTypeAndCategories() }
                        )
                    }
                    if (expenses.isNotEmpty()) {
                        database.expenseDao().upsertAll(
                            expenses.map { it.toRoomEntity() }
                        )
                    }
                    if (spendingLimits.isNotEmpty()) {
                        database.spendingLimitDao().upsertAll(
                            spendingLimits.map { it.toRoomEntity() }
                        )
                    }

                    keySettings.setRealmMigrated(true)
                    AppLogger.d(
                        "RealmToRoomMigrator",
                        "Migrated types=${types.size}, expenses=${expenses.size}, limits=${spendingLimits.size}"
                    )
                } finally {
                    if (!realm.isClosed()) {
                        realm.close()
                    }
                }
            }
        }
    }
}
