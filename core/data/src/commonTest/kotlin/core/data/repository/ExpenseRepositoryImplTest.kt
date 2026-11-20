package core.data.repository

import core.data.database.dao.ExpenseDao
import core.data.database.entity.ExpenseRoomEntity
import core.domain.model.Expense
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private class FakeExpenseDao : ExpenseDao {
    private val items = MutableStateFlow<List<ExpenseRoomEntity>>(emptyList())

    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<ExpenseRoomEntity>> =
        items.map { list -> list.filter { it.timestamp in startTimeOfMonth..endTimeOfMonth } }

    override fun getNonZeroCategoryExpensesDesc(): Flow<List<ExpenseRoomEntity>> =
        items.map { list -> list.filter { it.categoryId != 0 }.sortedByDescending { it.timestamp } }

    override fun getExpenseByTypeId(typeId: Long): Flow<List<ExpenseRoomEntity>> =
        items.map { list -> list.filter { it.typeId == typeId } }

    override fun getExpenseById(id: String): Flow<ExpenseRoomEntity?> =
        items.map { list -> list.find { it.id == id } }

    override fun getAllFlow(): Flow<List<ExpenseRoomEntity>> = items

    override suspend fun getAllOnce(): List<ExpenseRoomEntity> = items.value

    override suspend fun upsert(expense: ExpenseRoomEntity) {
        items.update { list -> list.filterNot { it.id == expense.id } + expense }
    }

    override suspend fun upsertAll(expenses: List<ExpenseRoomEntity>) {
        val ids = expenses.map { it.id }.toSet()
        items.update { list -> list.filterNot { it.id in ids } + expenses }
    }

    override suspend fun deleteById(id: String) {
        items.update { list -> list.filterNot { it.id == id } }
    }
}

class ExpenseRepositoryImplTest {

    @Test
    fun getRecentlyExpenses_filtersZeroCategory_deduplicatesByDescription_andLimitsTo8() = runTest {
        val dao = FakeExpenseDao()
        val repo = ExpenseRepositoryImpl(dao)

        // Category 0 should be excluded
        repo.insert(Expense(id = "0", categoryId = 0, typeId = 1L, description = "Ignored", timestamp = 999L))
        // Duplicate description should keep newest timestamp
        repo.insert(Expense(id = "1", categoryId = 10, typeId = 1L, description = "Coffee", timestamp = 100L))
        repo.insert(Expense(id = "2", categoryId = 10, typeId = 1L, description = "Coffee", timestamp = 500L))
        // 9 more distinct items -> total 10 distinct, expect top 8
        for (i in 3..11) {
            repo.insert(
                Expense(
                    id = i.toString(),
                    categoryId = 10,
                    typeId = 1L,
                    description = "Item $i",
                    timestamp = i * 10L
                )
            )
        }

        val recent = repo.getRecentlyExpenses().first()
        assertEquals(8, recent.size)
        assertEquals("Coffee", recent.first().description)
        assertEquals(500L, recent.first().timestamp)
    }

    @Test
    fun restore_skipsExistingTimestamps_andInsertsNewOnes() = runTest {
        val dao = FakeExpenseDao()
        val repo = ExpenseRepositoryImpl(dao)

        repo.insert(Expense(id = "existing", categoryId = 1, typeId = 1L, description = "Lunch", timestamp = 1000L))

        repo.restore(
            listOf(
                Expense(id = "dup", categoryId = 1, typeId = 1L, description = "Lunch Dup", timestamp = 1000L),
                Expense(id = "new1", categoryId = 2, typeId = 1L, description = "Dinner", timestamp = 2000L)
            )
        )

        val all = repo.getAll().first()
        assertEquals(2, all.size)
        assertEquals(setOf("existing", "new1"), all.map { it.id }.toSet())
    }
}
