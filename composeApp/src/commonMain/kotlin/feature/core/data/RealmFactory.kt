package feature.core.data

import feature.core.data.model.CategoryEntity
import feature.core.data.model.ExpenseEntity
import feature.core.data.model.TypeEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class RealmFactory{

    private var realm: Realm? = null

    fun build(): Realm {
        if (realm == null || realm!!.isClosed()) {
            val config = RealmConfiguration.Builder(
                schema = setOf(
                    ExpenseEntity::class,
                    CategoryEntity::class,
                    TypeEntity::class
                )
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
        return realm!!
    }
}