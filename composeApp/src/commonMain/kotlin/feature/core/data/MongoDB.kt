package feature.core.data

import feature.core.data.model.CategoryEntity
import feature.core.data.model.ExpenseEntity
import feature.core.data.model.SpendingLimitEntity
import feature.core.data.model.TypeEntity
import feature.core.domain.mapper.toExpense
import feature.core.domain.mapper.toExpenseEntity
import feature.core.domain.model.Expense
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

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