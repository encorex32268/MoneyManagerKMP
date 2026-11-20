package core.data.repository

import core.data.database.dao.ExpenseDao
import core.data.database.mapper.toExpense
import core.data.database.mapper.toRoomEntity
import core.domain.model.Expense
import core.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class ExpenseRepositoryImpl(
    private val expenseDao: ExpenseDao
) : ExpenseRepository {

    override fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Expense>> {
        return expenseDao.getExpenseByTime(
            startTimeOfMonth = startTimeOfMonth,
            endTimeOfMonth = endTimeOfMonth
        ).map { list ->
            list.map { it.toExpense() }
        }
    }

    override fun getRecentlyExpenses(): Flow<List<Expense>> {
        return expenseDao.getNonZeroCategoryExpensesDesc().map { list ->
            list.map { it.toExpense() }
                .distinctBy { it.description }
                .take(8)
        }
    }

    override fun getExpenseByTypeId(typeId: Long): Flow<List<Expense>> {
        return expenseDao.getExpenseByTypeId(typeId).map { list ->
            list.map { it.toExpense() }
        }
    }

    override fun getExpense(expense: Expense): Flow<Expense?> {
        return expenseDao.getExpenseById(expense.id).map { entity ->
            entity?.toExpense()
        }
    }

    override suspend fun delete(expense: Expense) {
        expenseDao.deleteById(expense.id)
    }

    override suspend fun update(expense: Expense) {
        expenseDao.upsert(expense.toRoomEntity())
    }

    override suspend fun insert(expense: Expense) {
        expenseDao.upsert(expense.toRoomEntity())
    }

    override fun getAll(): Flow<List<Expense>> {
        return expenseDao.getAllFlow().map { list ->
            list.map { it.toExpense() }
        }
    }

    override suspend fun restore(expenseList: List<Expense>) {
        val dbTimestamps = expenseDao.getAllOnce().map { it.timestamp.toString() }.toSet()
        val restoreExpenses = expenseList.filterNot { it.timestamp.toString() in dbTimestamps }
        if (restoreExpenses.isNotEmpty()) {
            expenseDao.upsertAll(restoreExpenses.map { it.toRoomEntity() })
        }
    }
}
