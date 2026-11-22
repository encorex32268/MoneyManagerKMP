package feature.chart.data.repository

import core.domain.model.Expense
import core.domain.model.Type
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private class FakeChartExpenseRepository : ExpenseRepository {
    val expenses = MutableStateFlow<List<Expense>>(emptyList())
    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<Expense>> =
        expenses.map { list -> list.filter { it.timestamp in startTimeOfMonth..endTimeOfMonth } }
    override fun getRecentlyExpenses(): Flow<List<Expense>> = expenses
    override fun getExpenseByTypeId(typeId: Long): Flow<List<Expense>> = expenses
    override fun getExpense(expense: Expense): Flow<Expense?> = expenses.map { it.firstOrNull() }
    override suspend fun delete(expense: Expense) {}
    override suspend fun update(expense: Expense) {}
    override suspend fun insert(expense: Expense) {}
    override fun getAll(): Flow<List<Expense>> = expenses
    override suspend fun restore(expenseList: List<Expense>) {}
}

private class FakeChartTypeRepository : TypeRepository {
    val types = MutableStateFlow<List<Type>>(emptyList())
    override fun getTypes(): Flow<List<Type>> = types
    override suspend fun update(type: Type) {}
    override suspend fun insert(type: Type) {}
    override suspend fun delete(type: Type) {}
    override suspend fun updateAllSortedTypes(types: List<Type>) {}
    override suspend fun restore(types: List<Type>) {}
}

class ChartRepositoryImplTest {

    @Test
    fun `getExpenseByTime excludes hidden types income items and empty type groups`() = runTest {
        val expenseRepo = FakeChartExpenseRepository()
        val typeRepo = FakeChartTypeRepository()

        val food = Type(id = "t1", typeIdTimestamp = 100L, name = "Food", colorArgb = -1, order = 0, isShow = true)
        val emptyType = Type(id = "t2", typeIdTimestamp = 200L, name = "Empty", colorArgb = -2, order = 1, isShow = true)
        val hiddenType = Type(id = "t3", typeIdTimestamp = 300L, name = "Hidden", colorArgb = -3, order = 2, isShow = false)
        typeRepo.types.value = listOf(food, emptyType, hiddenType)

        expenseRepo.expenses.value = listOf(
            Expense(id = "e1", categoryId = 1, typeId = 100L, description = "Lunch", cost = 120L, timestamp = 500L),
            Expense(id = "e2", categoryId = 1, typeId = 100L, description = "Refund", isIncome = true, cost = 50L, timestamp = 500L),
            Expense(id = "e3", categoryId = 2, typeId = 300L, description = "Hidden", cost = 300L, timestamp = 500L)
        )

        val repo = ChartRepositoryImpl(expenseRepo, typeRepo)
        val charts = repo.getExpenseByTime(0L, 1000L).first()

        assertEquals(1, charts.size)
        assertEquals(food, charts.first().type)
        assertEquals(listOf("e1"), charts.first().items.map { it.id })
    }
}
