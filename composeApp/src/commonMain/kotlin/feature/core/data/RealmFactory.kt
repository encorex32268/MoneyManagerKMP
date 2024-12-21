package feature.core.data

import feature.core.data.model.CategoryEntity
import feature.core.data.model.ExpenseEntity
import feature.core.data.model.SpendingLimitEntity
import feature.core.data.model.TypeEntity
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