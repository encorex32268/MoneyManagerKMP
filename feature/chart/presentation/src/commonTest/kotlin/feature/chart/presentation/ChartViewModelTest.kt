package feature.chart.presentation

import core.domain.model.Expense
import core.domain.model.SpendingLimit
import core.domain.model.Type
import core.domain.model.chart.Chart
import core.domain.repository.SpendingLimitRepository
import core.presentation.date.toTimestamp
import feature.chart.domain.repository.ChartRepository
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

private class FakeChartRepository : ChartRepository {
    var chartsProvider: (Long, Long) -> List<Chart> = { _, _ -> emptyList() }
    private val trigger = MutableStateFlow(0)

    override fun getExpenseByTime(startTimeOfMonth: Long, endTimeOfMonth: Long): Flow<List<Chart>> =
        trigger.map { chartsProvider(startTimeOfMonth, endTimeOfMonth) }
}

private class FakeChartSpendingLimitRepository : SpendingLimitRepository {
    val limits = MutableStateFlow<List<SpendingLimit>>(emptyList())
    override fun getAll(): Flow<List<SpendingLimit>> = limits
    override fun getSpendingLimit(year: Int, month: Int): Flow<SpendingLimit?> =
        limits.map { list -> list.firstOrNull { it.year == year && it.month == month } }
    override suspend fun insert(spendingLimit: SpendingLimit) {}
    override suspend fun update(spendingLimit: SpendingLimit) {}
    override suspend fun delete(spendingLimit: SpendingLimit) {}
    override suspend fun restore(spendingLimits: List<SpendingLimit>) {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class ChartViewModelTest {

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
    fun `sorts chart groups descending by sum of cost and computes totalExpense`() = runTest {
        val food = Type(id = "t1", typeIdTimestamp = 1L, name = "Food", colorArgb = -1, order = 0)
        val travel = Type(id = "t2", typeIdTimestamp = 2L, name = "Travel", colorArgb = -2, order = 1)
        val ts = LocalDateTime(2025, 8, 15, 10, 0, 0).toTimestamp()

        val chartRepo = FakeChartRepository().apply {
            chartsProvider = { start, end ->
                if (ts in start..end) {
                    listOf(
                        Chart(type = food, items = listOf(Expense(id = "1", categoryId = 1, typeId = 1L, description = "Tea", cost = 50L, timestamp = ts))),
                        Chart(type = travel, items = listOf(Expense(id = "2", categoryId = 2, typeId = 2L, description = "Train", cost = 1200L, timestamp = ts)))
                    )
                } else emptyList()
            }
        }
        val limitRepo = FakeChartSpendingLimitRepository().apply {
            limits.value = listOf(SpendingLimit(id = "l1", year = 2025, month = 8, limit = 5000L))
        }

        val vm = ChartViewModel(chartRepo, limitRepo)
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        vm.onAction(ChartAction.OnDatePick(year = 2025, month = 8))

        val state = vm.state.value
        assertEquals(listOf("Travel", "Food"), state.items.map { it.type.name })
        assertEquals(1250L, state.totalExpense)
        assertEquals(5000L, state.spendingLimit)
        assertEquals("2025", state.nowDateYear)
        assertEquals("8", state.nowDateMonth)

        job.cancel()
    }
}
