package feature.core.data.repository

import feature.core.data.model.ExpenseEntity
import feature.core.domain.mapper.toExpense
import feature.core.domain.mapper.toExpenseEntity
import feature.core.domain.model.Expense
import feature.core.domain.repository.ExpenseRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val realm: Realm
): ExpenseRepository {

    override fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Expense>> {
        return realm.query(
            clazz = ExpenseEntity::class,
            query = "timestamp < $0 AND timestamp > $1",
            endTimeOfMonth,
            startTimeOfMonth
        ).asFlow().map {
           it.list.toList().map { expenseEntity ->
             expenseEntity.toExpense()
          }
        }
    }

    override fun getRecentlyExpenses(): Flow<List<Expense>> {
        return realm.query(
            clazz = ExpenseEntity::class,
            query = "categoryId != 0"
        )
            .sort("timestamp", Sort.DESCENDING)
            .limit(8)
            .find().asFlow().map {
                it.list.toList().map {
                    it.toExpense()
                }
            }
    }

    override fun getExpenseByTypeId(typeId: Long): Flow<List<Expense>> {
        return realm.query(
            clazz = ExpenseEntity::class,
            query = "typeId == $0",typeId
        ).asFlow().map {
            it.list.map {
                it.toExpense()
            }
        }
    }

    override suspend fun delete(expense: Expense) {
        realm.write {
            val queriedExpenseEntity = query<ExpenseEntity>(
                query = "id == $0", expense.id
            )
                .first()
                .find()
            if (queriedExpenseEntity == null) return@write
            findLatest(queriedExpenseEntity)?.let { currentExpense ->
                delete(currentExpense)
            }
        }
    }

    override suspend fun update(expense: Expense) {
        realm.write {
            try {
                val queriedTask = query<ExpenseEntity>(
                    query = "id == $0", expense.id
                )
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
                e.printStackTrace()
                println(e)
            }
        }
    }

    override suspend fun insert(expense: Expense) {
        realm.write {
            copyToRealm(expense.toExpenseEntity())
        }
    }
}