package core.data

import core.data.model.CategoryEntity
import core.data.model.ExpenseEntity
import core.data.model.SpendingLimitEntity
import core.data.model.TypeEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration

class MongoDB{
    private var realm: Realm? = null

    init {
        configureTheRealm()
    }

    private fun configureTheRealm() {
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
                .build()
            realm = Realm.open(config)
        }
    }

}