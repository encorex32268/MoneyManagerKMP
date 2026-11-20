package core.data.repository

import core.data.model.ExpenseEntity
import core.domain.mapper.toExpense
import core.domain.mapper.toExpenseEntity
import core.domain.model.Expense
import core.domain.repository.ExpenseRepository
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.query.Sort
import kotlinx.coroutines.flow.Flow
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
            query = "timestamp <= $0 AND timestamp >= $1",
            endTimeOfMonth,
            startTimeOfMonth
        ).asFlow().map {
           if (it.list.isEmpty()){
               emptyList()
           }else{
               it.list.toList().map { expenseEntity ->
                   expenseEntity.toExpense()
               }
           }
        }
    }

    override fun getRecentlyExpenses(): Flow<List<Expense>> {
        return realm.query(
            clazz = ExpenseEntity::class,
            query = "categoryId != 0"
        )
            .sort("timestamp", Sort.DESCENDING)
            .distinct("description")
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
            if (it.list.isEmpty()){
                emptyList()
            }else{
                it.list.map {
                    it.toExpense()
                }
            }
        }
    }

    override fun getExpense(expense: Expense): Flow<Expense?> {
        return realm.query(
            clazz = ExpenseEntity::class,
            query = "id == $0",expense.id
        ).find().asFlow().map {
           if (it.list.isEmpty()){
               null
           }else{
               it.list.first().toExpense()
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
                    content = expense.content
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println(e)
            }
        }
    }

    override suspend fun insert(expense: Expense) {
        realm.write {
            copyToRealm(
                instance = expense.toExpenseEntity(),
            )
        }
    }



    override fun getAll(): Flow<List<Expense>> {
       return realm.query<ExpenseEntity>().find().asFlow().map {
            if (it.list.isEmpty()){
                emptyList()
            }else{
                it.list.toList().map { expenseEntity ->
                    expenseEntity.toExpense()
                }
            }
        }

    }

    override suspend fun restore(
        expenseList: List<Expense>
    ) {
        realm.write {
            val dbExpenses = this.query<ExpenseEntity>().find().map { it.toExpense() }
            val dbExpensesIdStringList = dbExpenses.map { it.timestamp.toString() }
            val restoreExpenses = expenseList.filterNot { it.timestamp.toString() in dbExpensesIdStringList }
            restoreExpenses.forEach {
                this.copyToRealm(
                    instance = it.toExpenseEntity(),
                    updatePolicy = UpdatePolicy.ALL
                )
            }
        }
    }
}