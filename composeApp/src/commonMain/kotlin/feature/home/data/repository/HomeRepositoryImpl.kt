package feature.home.data.repository

import feature.core.data.model.ExpenseEntity
import feature.core.domain.mapper.toExpense
import feature.core.domain.mapper.toExpenseEntity
import feature.core.domain.model.Expense
import feature.home.domain.repository.HomeRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class HomeRepositoryImpl(
    private val realm: Realm
) : HomeRepository {
    override suspend fun insertExpense(expense: Expense) {

        realm.write {
            copyToRealm(expense.toExpenseEntity())
        }
    }

    override suspend fun getExpenseByStartTimeAndEndTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ):  Flow<List<Expense>>{
        return realm.query(
            clazz = ExpenseEntity::class,
            query = "timestamp < $0 AND timestamp > $1",
            endTimeOfMonth ,
            startTimeOfMonth
        ).asFlow()
            .map {
                it.list.toList().map {expenseEntity ->
                    expenseEntity.toExpense()
                }
            }
    }

    override suspend fun updateExpense(expense: Expense) {
        realm.write {
            var expenseEntity = query<ExpenseEntity>("id == $0", expense.id).find().first()
            expenseEntity = expense.toExpenseEntity()
        }
    }

}