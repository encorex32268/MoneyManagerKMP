package feature.chart.presentation.chartdetail

import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.date.toTimestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
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

@OptIn(ExperimentalCoroutinesApi::class)
class DetailViewModelTest {

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
    fun `groups items by day descending and sums total`() = runTest {
        val type = Type(id = "t1", typeIdTimestamp = 1L, name = "Food", colorArgb = -1, order = 0)
        val ts1 = LocalDateTime(2025, 4, 1, 9, 0, 0).toTimestamp()
        val ts2 = LocalDateTime(2025, 4, 2, 18, 0, 0).toTimestamp()

        val vm = DetailViewModel(
            items = listOf(
                Expense(id = "e1", categoryId = 1, typeId = 1L, description = "Coffee", cost = 100L, timestamp = ts1),
                Expense(id = "e2", categoryId = 1, typeId = 1L, description = "Steak", cost = 850L, timestamp = ts2)
            ),
            type = type
        )
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        assertEquals(950L, vm.state.value.total)
        assertEquals(2, vm.state.value.items.size)
        assertEquals("e2", vm.state.value.items.first().second.first().id)

        job.cancel()
    }

    @Test
    fun `empty items list produces zero total`() = runTest {
        val type = Type(id = "t1", typeIdTimestamp = 1L, name = "Food", colorArgb = -1, order = 0)
        val vm = DetailViewModel(items = emptyList(), type = type)
        val job = backgroundScope.launch(testDispatcher) { vm.state.collect {} }

        assertEquals(0L, vm.state.value.total)
        assertEquals(emptyList(), vm.state.value.items)

        job.cancel()
    }
}
