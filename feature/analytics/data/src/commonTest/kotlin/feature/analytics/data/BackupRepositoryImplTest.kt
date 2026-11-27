package feature.analytics.data

import core.domain.model.Category
import core.domain.model.Expense
import core.domain.model.SpendingLimit
import core.domain.model.Type
import core.domain.repository.ExpenseRepository
import core.domain.repository.SpendingLimitRepository
import core.domain.repository.TypeRepository
import de.halfbit.csv.parseCsv
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class FakeBackupExpenseRepository : ExpenseRepository {
    private val items = MutableStateFlow<List<Expense>>(emptyList())
    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<Expense>> = items
    override fun getRecentlyExpenses(): Flow<List<Expense>> = items
    override fun getExpenseByTypeId(typeId: Long): Flow<List<Expense>> = items
    override fun getExpense(expense: Expense): Flow<Expense?> = MutableStateFlow(null)
    override suspend fun delete(expense: Expense) {}
    override suspend fun update(expense: Expense) {}
    override suspend fun insert(expense: Expense) {}
    override fun getAll(): Flow<List<Expense>> = items
    override suspend fun restore(expenseList: List<Expense>) {
        items.value = expenseList
    }
}

private class FakeBackupTypeRepository : TypeRepository {
    private val items = MutableStateFlow<List<Type>>(emptyList())
    override fun getTypes(): Flow<List<Type>> = items
    override suspend fun update(type: Type) {}
    override suspend fun insert(type: Type) {}
    override suspend fun delete(type: Type) {}
    override suspend fun updateAllSortedTypes(types: List<Type>) {}
    override suspend fun restore(types: List<Type>) {
        items.value = types
    }
}

private class FakeBackupSpendingLimitRepository : SpendingLimitRepository {
    private val items = MutableStateFlow<List<SpendingLimit>>(emptyList())
    override fun getAll(): Flow<List<SpendingLimit>> = items
    override fun getSpendingLimit(year: Int, month: Int): Flow<SpendingLimit?> = MutableStateFlow(null)
    override suspend fun insert(spendingLimit: SpendingLimit) {}
    override suspend fun update(spendingLimit: SpendingLimit) {}
    override suspend fun delete(spendingLimit: SpendingLimit) {}
    override suspend fun restore(spendingLimits: List<SpendingLimit>) {
        items.value = spendingLimits
    }
}

class BackupRepositoryImplTest {

    private fun createRepository(): BackupRepositoryImpl {
        return BackupRepositoryImpl(
            expenseRepository = FakeBackupExpenseRepository(),
            typeRepository = FakeBackupTypeRepository(),
            spendingLimitRepository = FakeBackupSpendingLimitRepository(),
            coroutineScope = CoroutineScope(SupervisorJob())
        )
    }

    @Test
    fun `buildCsv and parse round-trip preserves ids flags categories and spending limits`() {
        val repo = createRepository()

        val expenses = listOf(
            Expense(
                id = "exp-uuid-1",
                idString = "exp-uuid-1",
                categoryId = 10012,
                typeId = 1752673560278L,
                description = "Lunch",
                isIncome = false,
                cost = 250L,
                timestamp = 1752673600000L,
                content = "Ramen"
            ),
            Expense(
                id = "exp-uuid-2",
                idString = "exp-uuid-2",
                categoryId = 10099,
                typeId = 1752673560279L,
                description = "Salary",
                isIncome = true,
                cost = 50000L,
                timestamp = 1752673700000L,
                content = "Monthly"
            )
        )
        val types = listOf(
            Type(
                id = "type-uuid-1",
                typeIdHex = "type-uuid-1",
                typeIdTimestamp = 1752673560278L,
                name = "Food",
                colorArgb = -1710479,
                order = 0,
                isShow = true,
                categories = listOf(
                    Category(id = 10012, name = "Dining", order = 0, typeId = 1752673560278L)
                )
            ),
            Type(
                id = "type-uuid-2",
                typeIdHex = "",
                typeIdTimestamp = 1752673560279L,
                name = "HiddenType",
                colorArgb = -65536,
                order = 1,
                isShow = false,
                categories = emptyList()
            )
        )
        val limits = listOf(
            SpendingLimit(id = "limit-uuid-1", year = 2025, month = 7, limit = 30000L)
        )

        val csvText = repo.buildCsv(expenses, types, limits).toCsvText()
        val parsed = repo.parse(parseCsv(csvText))

        assertEquals(2, parsed.expenses.size)
        assertEquals("exp-uuid-1", parsed.expenses[0].id)
        assertEquals("exp-uuid-1", parsed.expenses[0].idString)
        assertFalse(parsed.expenses[0].isIncome)
        assertEquals(250L, parsed.expenses[0].cost)
        assertEquals("exp-uuid-2", parsed.expenses[1].id)
        assertTrue(parsed.expenses[1].isIncome)
        assertEquals(50000L, parsed.expenses[1].cost)

        assertEquals(2, parsed.types.size)
        assertEquals("type-uuid-1", parsed.types[0].id)
        assertEquals("type-uuid-1", parsed.types[0].typeIdHex)
        assertTrue(parsed.types[0].isShow)
        assertEquals(1, parsed.types[0].categories.size)
        assertEquals("Dining", parsed.types[0].categories[0].name)

        // Blank typeIdHex falls back to type.id on export and preserves isShow = false
        assertEquals("type-uuid-2", parsed.types[1].id)
        assertEquals("type-uuid-2", parsed.types[1].typeIdHex)
        assertFalse(parsed.types[1].isShow)

        assertEquals(1, parsed.spendingLimits.size)
        assertEquals("limit-uuid-1", parsed.spendingLimits[0].id)
        assertEquals(2025, parsed.spendingLimits[0].year)
        assertEquals(7, parsed.spendingLimits[0].month)
        assertEquals(30000L, parsed.spendingLimits[0].limit)
    }

    @Test
    fun `parse generates fallback uuid when expense Object_id is blank and supports legacy hex ids`() {
        val repo = createRepository()
        val legacyCsvText = buildString {
            appendLine("No,Type,Category,Description,Cost,Date,IsIncome,Content,Object_id,Type_No,Type_Date,Type_Name,Type_color_argb,Type_order,Type_is_show,Type_categories,Type_id,ID,Year,Month,Limit")
            // Expense with blank Object_id and invalid IsIncome
            appendLine("0,1752673560278,10012,Coffee,120,1752673600000&2025-07-16,,Note,,")
            // Legacy Realm hex ObjectId
            appendLine("1,1752673560278,10012,Bonus,5000,1752673700000&2025-07-16,true,Work,65a1b2c3d4e5f60708090a0b,")
            // Legacy Type row with Realm hex Type_id and empty Type_is_show (defaults to true)
            appendLine(",,,,,,,,,0,1752673560278&2025-07-16,Food,-1710479,0,,[],65a1b2c3d4e5f60708090a0c")
            // Legacy SpendingLimit row with Realm hex ID
            appendLine(",,,,,,,,,,,,,,,,,65a1b2c3d4e5f60708090a0d,2025,7,25000")
        }

        val parsed = repo.parse(parseCsv(legacyCsvText))

        assertEquals(2, parsed.expenses.size)
        assertTrue(parsed.expenses[0].id.isNotBlank())
        assertEquals(parsed.expenses[0].id, parsed.expenses[0].idString)
        assertFalse(parsed.expenses[0].isIncome)

        assertEquals("65a1b2c3d4e5f60708090a0b", parsed.expenses[1].id)
        assertEquals("65a1b2c3d4e5f60708090a0b", parsed.expenses[1].idString)
        assertTrue(parsed.expenses[1].isIncome)

        assertEquals(1, parsed.types.size)
        assertEquals("65a1b2c3d4e5f60708090a0c", parsed.types[0].id)
        assertEquals("65a1b2c3d4e5f60708090a0c", parsed.types[0].typeIdHex)
        assertTrue(parsed.types[0].isShow)

        assertEquals(1, parsed.spendingLimits.size)
        assertEquals("65a1b2c3d4e5f60708090a0d", parsed.spendingLimits[0].id)
        assertEquals(25000L, parsed.spendingLimits[0].limit)
    }
}
