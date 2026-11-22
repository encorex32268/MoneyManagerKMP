package feature.home.presentation

import core.domain.model.Expense
import core.domain.model.SpendingLimit
import core.domain.model.Type
import core.domain.repository.SpendingLimitRepository
import core.presentation.date.toTimestamp
import core.ui.limitsColor_0_50_Color
import core.ui.limitsColor_100_Color
import core.ui.limitsColor_50_70_Color
import core.ui.limitsColor_70_90_Color
import feature.home.domain.repository.HomeRepository
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
import kotlinx.datetime.LocalDateTime
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private class FakeHomeRepository : HomeRepository {
    val expenses = MutableStateFlow<List<Expense>>(emptyList())

    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<Expense>> =
        expenses.map { list -> list.filter { it.timestamp in startTimeOfMonth..endTimeOfMonth } }
}

private class FakeSpendingLimitRepository : SpendingLimitRepository {
    val limits = MutableStateFlow<List<SpendingLimit>>(emptyList())
    var insertCount = 0
    var updateCount = 0

    override fun getAll(): Flow<List<SpendingLimit>> = limits

    override fun getSpendingLimit(year: Int, month: Int): Flow<SpendingLimit?> =
        limits.map { list -> list.firstOrNull { it.year == year && it.month == month } }

    override suspend fun insert(spendingLimit: SpendingLimit) {
        insertCount++
        limits.value = limits.value + spendingLimit
    }

    override suspend fun update(spendingLimit: SpendingLimit) {
        updateCount++
        limits.value = limits.value.map { if (it.id == spendingLimit.id) spendingLimit else it }
    }

    override suspend fun delete(spendingLimit: SpendingLimit) {
        limits.value = limits.value.filterNot { it.id == spendingLimit.id }
    }

    override suspend fun restore(spendingLimits: List<SpendingLimit>) {
        limits.value = spendingLimits
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModelTest {

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
    fun `excludes income from totalExpense and sorts items descending by timestamp`() = runTest {
        val homeRepo = FakeHomeRepository()
        val limitRepo = FakeSpendingLimitRepository()
        val type = Type(id = "t1", typeIdTimestamp = 1L, name = "Food", colorArgb = -1, order = 0)

        val tsMorning = LocalDateTime(2025, 5, 10, 9, 0, 0).toTimestamp()
        val tsEvening = LocalDateTime(2025, 5, 10, 20, 0, 0).toTimestamp()
        homeRepo.expenses.value = listOf(
            Expense(id = "1", categoryId = 1, typeId = 1L, description = "Breakfast", cost = 100L, timestamp = tsMorning, type = type),
            Expense(id = "2", categoryId = 1, typeId = 1L, description = "Salary", isIncome = true, cost = 50000L, timestamp = tsEvening, type = type),
            Expense(id = "3", categoryId = 1, typeId = 1L, description = "Dinner", cost = 350L, timestamp = tsEvening, type = type)
        )

        val viewModel = HomeViewModel(homeRepo, limitRepo)
        val job = backgroundScope.launch(testDispatcher) { viewModel.state.collect {} }

        viewModel.onAction(HomeAction.OnDatePick(year = 2025, month = 5))
        val state = viewModel.state.value

        assertEquals(450L, state.totalExpense)
        assertEquals("2025", state.nowDateYear)
        assertEquals("5", state.nowDateMonth)
        assertEquals(1, state.items.size)
        val dayItems = state.items.first().second
        assertEquals(listOf("3", "1"), dayItems.map { it.id })

        job.cancel()
    }

    @Test
    fun `OnSpendingLimitChange inserts when none exists and updates when one exists`() = runTest {
        val homeRepo = FakeHomeRepository()
        val limitRepo = FakeSpendingLimitRepository()
        val viewModel = HomeViewModel(homeRepo, limitRepo)
        val job = backgroundScope.launch(testDispatcher) { viewModel.state.collect {} }

        viewModel.onAction(HomeAction.OnDatePick(year = 2025, month = 6))
        viewModel.onAction(HomeAction.OnSpendingLimitChange("10000"))

        assertEquals(1, limitRepo.insertCount)
        assertEquals(0, limitRepo.updateCount)
        assertEquals(10000L, viewModel.state.value.expenseLimit)

        viewModel.onAction(HomeAction.OnSpendingLimitChange("20000"))
        assertEquals(1, limitRepo.insertCount)
        assertEquals(1, limitRepo.updateCount)
        assertEquals(20000L, viewModel.state.value.expenseLimit)

        job.cancel()
    }

    @Test
    fun `OnSpendingLimitChange ignores non-numeric input`() = runTest {
        val homeRepo = FakeHomeRepository()
        val limitRepo = FakeSpendingLimitRepository()
        val viewModel = HomeViewModel(homeRepo, limitRepo)
        val job = backgroundScope.launch(testDispatcher) { viewModel.state.collect {} }

        viewModel.onAction(HomeAction.OnSpendingLimitChange(""))
        viewModel.onAction(HomeAction.OnSpendingLimitChange("abc"))
        viewModel.onAction(HomeAction.OnSpendingLimitChange("12.5"))

        assertEquals(0, limitRepo.insertCount)
        assertEquals(0, limitRepo.updateCount)

        job.cancel()
    }

    @Test
    fun `limitOverColor reflects threshold boundaries`() {
        assertEquals(limitsColor_0_50_Color, HomeState(totalExpense = 500, expenseLimit = 0).limitOverColor)
        assertEquals(limitsColor_0_50_Color, HomeState(totalExpense = 50, expenseLimit = 100).limitOverColor)
        assertEquals(limitsColor_50_70_Color, HomeState(totalExpense = 51, expenseLimit = 100).limitOverColor)
        assertEquals(limitsColor_70_90_Color, HomeState(totalExpense = 71, expenseLimit = 100).limitOverColor)
        assertEquals(limitsColor_100_Color, HomeState(totalExpense = 91, expenseLimit = 100).limitOverColor)
    }
}
