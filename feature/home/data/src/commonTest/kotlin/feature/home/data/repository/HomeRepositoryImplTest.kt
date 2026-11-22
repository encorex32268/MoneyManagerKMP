package feature.home.data.repository

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

private class FakeExpenseRepository : ExpenseRepository {
    val expenses = MutableStateFlow<List<Expense>>(emptyList())

    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<Expense>> =
        expenses.map { list -> list.filter { it.timestamp in startTimeOfMonth..endTimeOfMonth } }

    override fun getRecentlyExpenses(): Flow<List<Expense>> = expenses
    override fun getExpenseByTypeId(typeId: Long): Flow<List<Expense>> =
        expenses.map { list -> list.filter { it.typeId == typeId } }
    override fun getExpense(expense: Expense): Flow<Expense?> =
        expenses.map { list -> list.find { it.id == expense.id } }
    override suspend fun delete(expense: Expense) { expenses.value = expenses.value.filterNot { it.id == expense.id } }
    override suspend fun update(expense: Expense) { expenses.value = expenses.value.map { if (it.id == expense.id) expense else it } }
    override suspend fun insert(expense: Expense) { expenses.value = expenses.value + expense }
    override fun getAll(): Flow<List<Expense>> = expenses
    override suspend fun restore(expenseList: List<Expense>) { expenses.value = expenseList }
}

private class FakeTypeRepository : TypeRepository {
    val types = MutableStateFlow<List<Type>>(emptyList())

    override fun getTypes(): Flow<List<Type>> = types
    override suspend fun update(type: Type) { types.value = types.value.map { if (it.id == type.id) type else it } }
    override suspend fun insert(type: Type) { types.value = types.value + type }
    override suspend fun delete(type: Type) { types.value = types.value.filterNot { it.id == type.id } }
    override suspend fun updateAllSortedTypes(types: List<Type>) { this.types.value = types }
    override suspend fun restore(types: List<Type>) { this.types.value = types }
}

class HomeRepositoryImplTest {

    @Test
    fun `getExpenseByTime excludes hidden types and attaches matching Type`() = runTest {
        val expenseRepo = FakeExpenseRepository()
        val typeRepo = FakeTypeRepository()

        val shownType = Type(id = "t1", typeIdTimestamp = 100L, name = "Food", colorArgb = -1, order = 0, isShow = true)
        val hiddenType = Type(id = "t2", typeIdTimestamp = 200L, name = "Secret", colorArgb = -2, order = 1, isShow = false)
        typeRepo.types.value = listOf(shownType, hiddenType)

        expenseRepo.expenses.value = listOf(
            Expense(id = "e1", categoryId = 1, typeId = 100L, description = "Ramen", cost = 200, timestamp = 500L),
            Expense(id = "e2", categoryId = 2, typeId = 200L, description = "Hidden", cost = 300, timestamp = 600L),
            Expense(id = "e3", categoryId = 3, typeId = 999L, description = "Orphan", cost = 400, timestamp = 700L)
        )

        val repo = HomeRepositoryImpl(expenseRepo, typeRepo)
        val result = repo.getExpenseByTime(0L, 1000L).first()

        assertEquals(1, result.size)
        assertEquals("e1", result.first().id)
        assertEquals(shownType, result.first().type)
    }

    @Test
    fun `getExpenseByTime reacts when type visibility changes`() = runTest {
        val expenseRepo = FakeExpenseRepository()
        val typeRepo = FakeTypeRepository()

        val type = Type(id = "t1", typeIdTimestamp = 100L, name = "Food", colorArgb = -1, order = 0, isShow = true)
        typeRepo.types.value = listOf(type)
        expenseRepo.expenses.value = listOf(
            Expense(id = "e1", categoryId = 1, typeId = 100L, description = "Lunch", cost = 100, timestamp = 500L)
        )

        val repo = HomeRepositoryImpl(expenseRepo, typeRepo)
        assertEquals(1, repo.getExpenseByTime(0L, 1000L).first().size)

        typeRepo.types.value = listOf(type.copy(isShow = false))
        assertEquals(0, repo.getExpenseByTime(0L, 1000L).first().size)
    }
}
