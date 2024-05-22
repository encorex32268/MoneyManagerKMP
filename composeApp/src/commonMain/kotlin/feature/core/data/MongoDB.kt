package feature.core.data

import feature.core.data.model.ExpenseEntity
import feature.core.domain.mapper.toExpense
import feature.core.domain.mapper.toExpenseEntity
import feature.core.domain.model.Expense
import io.realm.kotlin.Realm
import io.realm.kotlin.RealmConfiguration
import io.realm.kotlin.ext.asFlow
import io.realm.kotlin.ext.query
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
                schema = setOf(ExpenseEntity::class)
            )
                .compactOnLaunch()
                .build()
            realm = Realm.open(config)
        }
    }

    fun getExpenseByStartTimeAndEndTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Expense>> {
//        val allRecords = realm?.query<ExpenseEntity>("TRUEPREDICATE")?.find()
//        allRecords?.forEach {
//            println(it) }

        return realm?.query(
            clazz = ExpenseEntity::class,
            query = "timestamp < $0 AND timestamp > $1",
            endTimeOfMonth,
            startTimeOfMonth
        )?.asFlow()
            ?.map {
                it.list.toList().map {
                    expenseEntity -> expenseEntity.toExpense()
                }
            }
            ?: flowOf(emptyList())
    }

    suspend fun updateExpense(expense: Expense) {
        realm?.write {
            try {
                val queriedTask = query<ExpenseEntity>(query = "id == $0", expense.id)
                    .find()
                    .first()
                queriedTask.apply {
                    cost = expense.cost
                    description = expense.description
                    categoryId = expense.categoryId
                    typeId = expense.typeId
                    isIncome = expense.isIncome
                    timestamp = expense.timestamp
                }
            } catch (e: Exception) {
                println(e)
            }
        }

    }

    suspend fun insertExpense(expense: Expense) {
        realm?.write {
            copyToRealm(expense.toExpenseEntity())
        }
    }

    suspend fun deleteExpense(expense: Expense) {
        val expenseEntity = expense.toExpenseEntity()
        realm?.write {
            try {
                val queriedExpenseEntity = query<ExpenseEntity>(query = "id == $0", expenseEntity.id)
                    .first()
                    .find()
                queriedExpenseEntity?.let {
                    findLatest(it)?.let { currentExpense ->
                        delete(currentExpense)
                    }
                }
            } catch (e: Exception) {
                println(e)
            }
        }
    }

    fun getExpense(expense: Expense): Expense? {
        return realm?.query(
            clazz = ExpenseEntity::class,
            query = "id == $0", expense.id
        )?.first()?.find()?.toExpense()
    }
}