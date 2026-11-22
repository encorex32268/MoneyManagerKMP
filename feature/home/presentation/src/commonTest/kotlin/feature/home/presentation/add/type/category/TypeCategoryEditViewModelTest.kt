package feature.home.presentation.add.type.category

import core.domain.model.Type
import core.domain.repository.TypeRepository
import core.presentation.model.CategoryUi
import feature.home.presentation.add.type.TypeUi
import feature.home.presentation.reorderable.ItemPosition
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

private class FakeCategoryEditTypeRepo : TypeRepository {
    val types = MutableStateFlow<List<Type>>(emptyList())
    var updateCount = 0
    var lastUpdated: Type? = null

    override fun getTypes(): Flow<List<Type>> = types
    override suspend fun update(type: Type) {
        updateCount++
        lastUpdated = type
    }
    override suspend fun insert(type: Type) {}
    override suspend fun delete(type: Type) {}
    override suspend fun updateAllSortedTypes(types: List<Type>) {}
    override suspend fun restore(types: List<Type>) {}
}

@OptIn(ExperimentalCoroutinesApi::class)
class TypeCategoryEditViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @BeforeTest
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun sampleTypeUi(): TypeUi = TypeUi(
        id = "t1",
        typeIdHex = "t1",
        typeIdTimestamp = 100L,
        name = "Food",
        colorArgb = -100,
        order = 0,
        isShow = true,
        categories = listOf(
            CategoryUi(id = 1, name = "Breakfast", order = 0, typeId = 100L, isClick = false, colorArgb = -100),
            CategoryUi(id = 2, name = "Lunch", order = 1, typeId = 100L, isClick = false, colorArgb = -100)
        )
    )

    @Test
    fun `OnBackClick without changes skips update and emits OnBack`() = runTest {
        val repo = FakeCategoryEditTypeRepo()
        val vm = TypeCategoryEditViewModel(sampleTypeUi(), repo)

        vm.onAction(TypeCategoryEditAction.OnBackClick)

        assertEquals(0, repo.updateCount)
        assertEquals(TypeCategoryEditUiEvent.OnBack, vm.uiEvent.first())
    }

    @Test
    fun `OnItemAdd rejects blank names and adds valid category with type color`() = runTest {
        val repo = FakeCategoryEditTypeRepo()
        val vm = TypeCategoryEditViewModel(sampleTypeUi(), repo)

        vm.onAction(TypeCategoryEditAction.OnItemAdd(CategoryUi(id = 3, name = "   ", order = 2, typeId = 100L, isClick = false)))
        assertEquals(2, vm.state.value.typeUi.categories.size)

        vm.onAction(TypeCategoryEditAction.OnItemAdd(CategoryUi(id = 3, name = "Dinner 🍜", order = 2, typeId = 100L, isClick = false)))
        assertEquals(3, vm.state.value.typeUi.categories.size)
        assertEquals("Dinner 🍜", vm.state.value.typeUi.categories.last().name)
        assertEquals(-100, vm.state.value.typeUi.categories.last().colorArgb)
    }

    @Test
    fun `OnItemMove reorders categories and OnSaveClick normalizes order indices`() = runTest {
        val repo = FakeCategoryEditTypeRepo()
        val vm = TypeCategoryEditViewModel(sampleTypeUi(), repo)

        vm.onAction(
            TypeCategoryEditAction.OnItemMove(
                fromItemPosition = ItemPosition(index = 0, key = 1),
                toItemPosition = ItemPosition(index = 1, key = 2)
            )
        )
        assertEquals(listOf("Lunch", "Breakfast"), vm.state.value.typeUi.categories.map { it.name })

        vm.onAction(TypeCategoryEditAction.OnSaveClick)
        assertEquals(1, repo.updateCount)
        assertEquals(listOf(0, 1), repo.lastUpdated?.categories?.map { it.order })
        assertEquals(TypeCategoryEditUiEvent.OnSavedShow, vm.uiEvent.first())
    }

    @Test
    fun `OnItemRemove removes item and OnBackClick persists changes`() = runTest {
        val repo = FakeCategoryEditTypeRepo()
        val initial = sampleTypeUi()
        val vm = TypeCategoryEditViewModel(initial, repo)

        vm.onAction(TypeCategoryEditAction.OnItemRemove(initial.categories.first()))
        assertEquals(listOf("Lunch"), vm.state.value.typeUi.categories.map { it.name })

        vm.onAction(TypeCategoryEditAction.OnBackClick)
        assertEquals(1, repo.updateCount)
        assertEquals(TypeCategoryEditUiEvent.OnBack, vm.uiEvent.first())
    }
}
