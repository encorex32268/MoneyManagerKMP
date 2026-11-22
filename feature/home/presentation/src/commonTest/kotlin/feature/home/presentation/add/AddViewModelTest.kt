package feature.home.presentation.add

import core.domain.KeySettings
import core.domain.model.Expense
import core.domain.model.Type
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import core.presentation.date.toTimestamp
import core.presentation.model.CategoryUi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlinx.datetime.LocalDateTime
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

private class FakeAddExpenseRepository : ExpenseRepository {
    val inserted = mutableListOf<Expense>()
    val updated = mutableListOf<Expense>()
    private val flow = MutableStateFlow<List<Expense>>(emptyList())

    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<Expense>> = flow
    override fun getRecentlyExpenses(): Flow<List<Expense>> = flow
    override fun getExpenseByTypeId(typeId: Long): Flow<List<Expense>> = flow
    override fun getExpense(expense: Expense): Flow<Expense?> = flow.map { it.firstOrNull() }
    override suspend fun delete(expense: Expense) {}
    override suspend fun update(expense: Expense) { updated.add(expense) }
    override suspend fun insert(expense: Expense) { inserted.add(expense) }
    override fun getAll(): Flow<List<Expense>> = flow
    override suspend fun restore(expenseList: List<Expense>) {}
}

private class FakeAddTypeRepository : TypeRepository {
    private val flow = MutableStateFlow<List<Type>>(emptyList())
    override fun getTypes(): Flow<List<Type>> = flow
    override suspend fun update(type: Type) {}
    override suspend fun insert(type: Type) {}
    override suspend fun delete(type: Type) {}
    override suspend fun updateAllSortedTypes(types: List<Type>) {}
    override suspend fun restore(types: List<Type>) {}
}

private class FakeKeySettings : KeySettings {
    var dark = false
    var migrated = false

    override fun setDarkLightMode(isDark: Boolean) { dark = isDark }
    override fun getDarkLightMode(): Boolean = dark
    override fun setRealmMigrated(isMigrated: Boolean) { migrated = isMigrated }
    override fun isRealmMigrated(): Boolean = migrated
}

@OptIn(ExperimentalCoroutinesApi::class)
class AddViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun createViewModel(
        expenseRepo: FakeAddExpenseRepository = FakeAddExpenseRepository(),
        typeRepo: FakeAddTypeRepository = FakeAddTypeRepository(),
        keySettings: FakeKeySettings = FakeKeySettings(),
        expense: Expense? = null
    ) = AddViewModel(
        repository = expenseRepo,
        typeRepository = typeRepo,
        keySettings = keySettings,
        expense = expense,
        recentlyLabelProvider = { "Recently" }
    )

    @Test
    fun `OnCostChange enforces max length and OnDeleteTextClick drops last char`() = runTest {
        val vm = createViewModel()
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        // Initial cost is "0", append digits up to > 10 chars
        repeat(15) {
            vm.onAction(AddAction.OnCostChange("9"))
        }
        assertEquals(11, vm.state.value.cost.length)

        vm.onAction(AddAction.OnDeleteTextClick)
        assertEquals(10, vm.state.value.cost.length)

        // Delete until empty, extra delete is a safe no-op
        repeat(15) {
            vm.onAction(AddAction.OnDeleteTextClick)
        }
        assertEquals("", vm.state.value.cost)
        job.cancel()
    }

    @Test
    fun `OnSelectedDate updates year monthNumber and dayOfMonth`() = runTest {
        val vm = createViewModel()
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }
        val ts = LocalDateTime(2024, 2, 29, 12, 0, 0).toTimestamp()

        vm.onAction(AddAction.OnSelectedDate(ts))

        assertEquals(2024, vm.state.value.year)
        assertEquals(2, vm.state.value.monthNumber)
        assertEquals(29, vm.state.value.dayOfMonth)
        job.cancel()
    }

    @Test
    fun `OnSaveClick inserts new expense when currentExpense is null and emits Success`() = runTest {
        val expenseRepo = FakeAddExpenseRepository()
        val vm = createViewModel(expenseRepo = expenseRepo)
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        vm.onAction(AddAction.OnItemSelected(CategoryUi(id = 10, typeId = 100L, name = "Food", order = 0, isClick = false), "Ramen 🍜", isRecently = false))
        vm.onAction(AddAction.OnCostChange("280"))
        vm.onAction(AddAction.OnSaveClick)

        assertEquals(1, expenseRepo.inserted.size)
        assertEquals(0, expenseRepo.updated.size)
        assertEquals(280L, expenseRepo.inserted.first().cost)
        assertEquals("Ramen 🍜", expenseRepo.inserted.first().description)
        assertEquals(AddUiEvent.Success, vm.uiEvent.first())
        job.cancel()
    }

    @Test
    fun `OnSaveClick updates existing expense when currentExpense is non-null`() = runTest {
        val existing = Expense(id = "exp-99", categoryId = 10, typeId = 100L, description = "Old", cost = 100L, content = "keep-note")
        val expenseRepo = FakeAddExpenseRepository()
        val vm = createViewModel(expenseRepo = expenseRepo, expense = existing)
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        vm.onAction(AddAction.OnDescriptionChange("New"))
        vm.onAction(AddAction.OnCostChange("500"))
        vm.onAction(AddAction.OnSaveClick)

        assertEquals(0, expenseRepo.inserted.size)
        assertEquals(1, expenseRepo.updated.size)
        assertEquals("exp-99", expenseRepo.updated.first().id)
        assertEquals("keep-note", expenseRepo.updated.first().content)
        assertEquals(100500L, expenseRepo.updated.first().cost)
        assertEquals(AddUiEvent.Success, vm.uiEvent.first())
        job.cancel()
    }
}
