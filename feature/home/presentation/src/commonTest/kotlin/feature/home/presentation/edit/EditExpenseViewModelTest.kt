package feature.home.presentation.edit

import core.domain.model.Expense
import core.domain.model.Type
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
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
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

private class FakeEditExpenseRepository : ExpenseRepository {
    val expenses = MutableStateFlow<List<Expense>>(emptyList())
    var updateCount = 0
    var deleteCount = 0

    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<Expense>> = expenses
    override fun getRecentlyExpenses(): Flow<List<Expense>> = expenses
    override fun getExpenseByTypeId(typeId: Long): Flow<List<Expense>> = expenses
    override fun getExpense(expense: Expense): Flow<Expense?> =
        expenses.map { list -> list.find { it.id == expense.id } }

    override suspend fun delete(expense: Expense) {
        deleteCount++
        expenses.value = expenses.value.filterNot { it.id == expense.id }
    }

    override suspend fun update(expense: Expense) {
        updateCount++
        expenses.value = expenses.value.map { if (it.id == expense.id) expense else it }
    }

    override suspend fun insert(expense: Expense) { expenses.value = expenses.value + expense }
    override fun getAll(): Flow<List<Expense>> = expenses
    override suspend fun restore(expenseList: List<Expense>) { expenses.value = expenseList }
}

private class FakeEditTypeRepository : TypeRepository {
    val types = MutableStateFlow<List<Type>>(emptyList())
    override fun getTypes(): Flow<List<Type>> = types
    override suspend fun update(type: Type) {}
    override suspend fun insert(type: Type) {}
    override suspend fun delete(type: Type) {}
    override suspend fun updateAllSortedTypes(types: List<Type>) {}
    override suspend fun restore(types: List<Type>) {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class EditExpenseViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loads expense and applies matching type color`() = runTest {
        val expense = Expense(id = "e1", categoryId = 1, typeId = 100L, description = "Lunch", cost = 150L, content = "initial")
        val expenseRepo = FakeEditExpenseRepository().apply { expenses.value = listOf(expense) }
        val typeRepo = FakeEditTypeRepository().apply {
            types.value = listOf(Type(id = "t1", typeIdTimestamp = 100L, name = "Food", colorArgb = -12345, order = 0))
        }

        val vm = EditExpenseViewModel(expense, expenseRepo, typeRepo)
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        val ui = vm.state.value.currentExpenseUi
        assertEquals("e1", ui?.id)
        assertEquals(-12345, ui?.colorRGB)
        assertFalse(vm.state.value.isShowSaveIcon)

        job.cancel()
    }

    @Test
    fun `missing expense leaves currentExpenseUi null`() = runTest {
        val expense = Expense(id = "missing", categoryId = 1, typeId = 100L, description = "None")
        val vm = EditExpenseViewModel(expense, FakeEditExpenseRepository(), FakeEditTypeRepository())
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        assertNull(vm.state.value.currentExpenseUi)
        job.cancel()
    }

    @Test
    fun `OnContentChange toggles isShowSaveIcon and OnSaveClick persists only when changed`() = runTest {
        val expense = Expense(id = "e1", categoryId = 1, typeId = 100L, description = "Lunch", cost = 150L, content = "old")
        val expenseRepo = FakeEditExpenseRepository().apply { expenses.value = listOf(expense) }
        val vm = EditExpenseViewModel(expense, expenseRepo, FakeEditTypeRepository())
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        vm.onAction(EditExpenseAction.OnContentChange("new note 📝"))
        assertTrue(vm.state.value.isShowSaveIcon)

        // Reverting back to original text hides save icon
        vm.onAction(EditExpenseAction.OnContentChange("old"))
        assertFalse(vm.state.value.isShowSaveIcon)

        // Save without change does not hit repository.update
        vm.onAction(EditExpenseAction.OnSaveClick)
        assertEquals(0, expenseRepo.updateCount)
        assertEquals(EditExpenseUiEvent.HideKeyboard, vm.uiEvent.first())

        // Modify and save persists to repository
        vm.onAction(EditExpenseAction.OnContentChange("updated"))
        vm.onAction(EditExpenseAction.OnSaveClick)
        assertEquals(1, expenseRepo.updateCount)
        assertFalse(vm.state.value.isShowSaveIcon)
        assertEquals("updated", expenseRepo.expenses.value.first().content)

        job.cancel()
    }

    @Test
    fun `OnDelete deletes expense and emits OnBack`() = runTest {
        val expense = Expense(id = "e1", categoryId = 1, typeId = 100L, description = "Lunch", cost = 150L)
        val expenseRepo = FakeEditExpenseRepository().apply { expenses.value = listOf(expense) }
        val vm = EditExpenseViewModel(expense, expenseRepo, FakeEditTypeRepository())
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        vm.onAction(EditExpenseAction.OnDelete)

        assertEquals(1, expenseRepo.deleteCount)
        assertTrue(expenseRepo.expenses.value.isEmpty())
        assertEquals(EditExpenseUiEvent.OnBack, vm.uiEvent.first())

        job.cancel()
    }
}
