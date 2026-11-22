package feature.home.presentation.add.type

import core.domain.model.Category
import core.domain.model.Expense
import core.domain.model.Type
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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

private class FakeTypeRepo : TypeRepository {
    val types = MutableStateFlow<List<Type>>(emptyList())
    var insertCount = 0
    var updateSortedCalled = false

    override fun getTypes(): Flow<List<Type>> = types
    override suspend fun update(type: Type) {
        types.value = types.value.map { if (it.id == type.id) type else it }
    }
    override suspend fun insert(type: Type) {
        insertCount++
        types.value = types.value + type
    }
    override suspend fun delete(type: Type) {
        types.value = types.value.filterNot { it.id == type.id }
    }
    override suspend fun updateAllSortedTypes(types: List<Type>) {
        updateSortedCalled = true
        this.types.value = types
    }
    override suspend fun restore(types: List<Type>) { this.types.value = types }
}

private class FakeTypeExpenseRepo : ExpenseRepository {
    val expenses = MutableStateFlow<List<Expense>>(emptyList())
    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<Expense>> = expenses
    override fun getRecentlyExpenses(): Flow<List<Expense>> = expenses
    override fun getExpenseByTypeId(typeId: Long): Flow<List<Expense>> =
        expenses.map { list -> list.filter { it.typeId == typeId } }
    override fun getExpense(expense: Expense): Flow<Expense?> = expenses.map { it.firstOrNull() }
    override suspend fun delete(expense: Expense) { expenses.value = expenses.value.filterNot { it.id == expense.id } }
    override suspend fun update(expense: Expense) {}
    override suspend fun insert(expense: Expense) {}
    override fun getAll(): Flow<List<Expense>> = expenses
    override suspend fun restore(expenseList: List<Expense>) {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class TypeViewModelTest {

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
    fun `splits shown and hidden types sorted by order`() = runTest {
        val typeRepo = FakeTypeRepo().apply {
            types.value = listOf(
                Type(id = "t2", typeIdTimestamp = 2L, name = "B", colorArgb = -1, order = 1, isShow = true),
                Type(id = "t1", typeIdTimestamp = 1L, name = "A", colorArgb = -1, order = 0, isShow = true),
                Type(id = "t3", typeIdTimestamp = 3L, name = "Hidden", colorArgb = -1, order = 0, isShow = false)
            )
        }
        val vm = TypeViewModel(typeRepo, FakeTypeExpenseRepo())
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        assertEquals(listOf("t1", "t2"), vm.state.value.items.map { it.id })
        assertEquals(listOf("t3"), vm.state.value.itemsNotShowing.map { it.id })

        job.cancel()
    }

    @Test
    fun `OnMoveUp swaps item with previous and ignores first item`() = runTest {
        val typeRepo = FakeTypeRepo().apply {
            types.value = listOf(
                Type(id = "t1", typeIdHex = "h1", typeIdTimestamp = 1L, name = "A", colorArgb = -1, order = 0, isShow = true),
                Type(id = "t2", typeIdHex = "h2", typeIdTimestamp = 2L, name = "B", colorArgb = -1, order = 1, isShow = true)
            )
        }
        val vm = TypeViewModel(typeRepo, FakeTypeExpenseRepo())
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        // Moving first item up is a no-op
        vm.onAction(TypeAction.OnMoveUp(vm.state.value.items.first()))
        assertEquals(false, typeRepo.updateSortedCalled)

        // Moving second item up swaps order
        vm.onAction(TypeAction.OnMoveUp(vm.state.value.items[1]))
        assertEquals(true, typeRepo.updateSortedCalled)
        assertEquals(listOf("t2", "t1"), vm.state.value.items.map { it.id })
        assertEquals(listOf(0, 1), vm.state.value.items.map { it.order })

        job.cancel()
    }

    @Test
    fun `OnNew rejects empty name or zero colorArgb`() = runTest {
        val typeRepo = FakeTypeRepo()
        val vm = TypeViewModel(typeRepo, FakeTypeExpenseRepo())

        vm.onAction(TypeAction.OnNew(TypeUi(name = "", colorArgb = -1, typeIdTimestamp = 1L, isShow = true, order = 0)))
        vm.onAction(TypeAction.OnNew(TypeUi(name = "Valid", colorArgb = 0, typeIdTimestamp = 2L, isShow = true, order = 0)))
        assertEquals(0, typeRepo.insertCount)

        vm.onAction(TypeAction.OnNew(TypeUi(name = "Valid", colorArgb = -1, typeIdTimestamp = 3L, isShow = true, order = 0)))
        assertEquals(1, typeRepo.insertCount)
    }

    @Test
    fun `OnDelete cascades deletion to expenses with matching typeId`() = runTest {
        val typeRepo = FakeTypeRepo().apply {
            types.value = listOf(
                Type(id = "t1", typeIdTimestamp = 100L, name = "Food", colorArgb = -1, order = 0, categories = listOf(Category(id = 1, name = "Lunch")))
            )
        }
        val expenseRepo = FakeTypeExpenseRepo().apply {
            expenses.value = listOf(
                Expense(id = "e1", categoryId = 1, typeId = 100L, description = "Ramen"),
                Expense(id = "e2", categoryId = 2, typeId = 200L, description = "Bus")
            )
        }
        val vm = TypeViewModel(typeRepo, expenseRepo)
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        vm.onAction(TypeAction.OnDelete(vm.state.value.items.first()))

        assertEquals(0, typeRepo.types.value.size)
        assertEquals(listOf("e2"), expenseRepo.expenses.value.map { it.id })

        job.cancel()
    }
}
