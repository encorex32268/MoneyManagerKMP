package feature.analytics.presentation

import core.domain.model.Expense
import core.presentation.date.toTimestamp
import feature.analytics.domain.AnalyticsRepository
import feature.analytics.presentation.model.DataPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
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
import kotlin.test.assertNull

private class FakeAnalyticsRepository : AnalyticsRepository {
    val expenses = MutableStateFlow<List<Expense>>(emptyList())
    override fun getAllExpense(): Flow<List<Expense>> = expenses
}

@OptIn(ExperimentalCoroutinesApi::class)
class AnalyticsViewModelTest {

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
    fun `ONE_MONTH filters by selected month excludes income and sums expenseSum`() = runTest {
        val repo = FakeAnalyticsRepository()
        val may1 = LocalDateTime(2025, 5, 1, 10, 0, 0).toTimestamp()
        val may2 = LocalDateTime(2025, 5, 2, 10, 0, 0).toTimestamp()
        val june1 = LocalDateTime(2025, 6, 1, 10, 0, 0).toTimestamp()

        repo.expenses.value = listOf(
            Expense(id = "1", categoryId = 1, typeId = 1L, description = "Lunch", cost = 200L, timestamp = may1),
            Expense(id = "2", categoryId = 1, typeId = 1L, description = "Salary", isIncome = true, cost = 9999L, timestamp = may1),
            Expense(id = "3", categoryId = 1, typeId = 1L, description = "Dinner", cost = 300L, timestamp = may2),
            Expense(id = "4", categoryId = 1, typeId = 1L, description = "June", cost = 500L, timestamp = june1)
        )

        val vm = AnalyticsViewModel(repo)
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        vm.onAction(AnalyticsAction.OnDateFilterChange(DateFilter.ONE_MONTH))
        vm.onAction(AnalyticsAction.OnDatePick(year = 2025, month = 5))

        val state = vm.state.value
        assertEquals("2025", state.nowDateYear)
        assertEquals("5", state.nowDateMonth)
        assertEquals(500L, state.expenseSum)
        assertEquals(listOf("5/1", "5/2"), state.dataPoints.map { it.xLabel })

        job.cancel()
    }

    @Test
    fun `ALL aggregates across months and ignores OnDatePick`() = runTest {
        val repo = FakeAnalyticsRepository()
        val jan = LocalDateTime(2025, 1, 10, 10, 0, 0).toTimestamp()
        val feb = LocalDateTime(2025, 2, 10, 10, 0, 0).toTimestamp()

        repo.expenses.value = listOf(
            Expense(id = "1", categoryId = 1, typeId = 1L, description = "Jan", cost = 100L, timestamp = jan),
            Expense(id = "2", categoryId = 1, typeId = 1L, description = "Feb", cost = 400L, timestamp = feb)
        )

        val vm = AnalyticsViewModel(repo)
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        vm.onAction(AnalyticsAction.OnDateFilterChange(DateFilter.ALL))
        vm.onAction(AnalyticsAction.OnDatePick(year = 2020, month = 1))

        assertEquals(DateFilter.ALL, vm.state.value.dateFilter)
        assertEquals(500L, vm.state.value.expenseSum)
        assertEquals(2, vm.state.value.dataPoints.size)

        job.cancel()
    }

    @Test
    fun `OnSelectDataPoint sets selection and OnDateFilterChange clears it`() = runTest {
        val vm = AnalyticsViewModel(FakeAnalyticsRepository())
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        val point = DataPoint(x = 1f, y = 200f, xLabel = "5/1")
        vm.onAction(AnalyticsAction.OnSelectDataPoint(point))
        assertEquals(point, vm.state.value.selectedDataPoint)

        vm.onAction(AnalyticsAction.OnDateFilterChange(DateFilter.SEVEN_DAYS))
        assertNull(vm.state.value.selectedDataPoint)

        job.cancel()
    }
}
