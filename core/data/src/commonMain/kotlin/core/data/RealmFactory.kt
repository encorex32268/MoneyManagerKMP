package core.data

import core.data.model.CategoryEntity
import core.data.model.ExpenseEntity
import core.data.model.SpendingLimitEntity
import core.data.model.TypeEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import migration_0_to_1

class RealmFactory{

    private var realm: Realm? = null

    fun build(): Realm {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    ExpenseEntity::class,
                    CategoryEntity::class,
                    TypeEntity::class,
                    SpendingLimitEntity::class
                )
            )
                .compactOnLaunch()
                .migration(migration_0_to_1)
                .schemaVersion(1)
                .build()
            realm = Realm.open(config)
        }
        return realm!!
    }
}