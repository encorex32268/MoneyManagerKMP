package core.data.repository

import core.data.database.dao.TypeDao
import core.data.database.entity.CategoryRoomEntity
import core.data.database.entity.TypeRoomEntity
import core.data.database.entity.TypeWithCategories
import core.domain.model.Category
import core.domain.model.Type
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

private class FakeTypeDao : TypeDao {
    private val types = MutableStateFlow<List<TypeRoomEntity>>(emptyList())
    private val categories = MutableStateFlow<List<CategoryRoomEntity>>(emptyList())

    override fun getTypes(): Flow<List<TypeWithCategories>> =
        types.map { typeList ->
            typeList.sortedBy { it.order }.map { type ->
                TypeWithCategories(
                    type = type,
                    categories = categories.value.filter { it.parentTypeId == type.id }
                )
            }
        }

    override suspend fun getTypesOnce(): List<TypeWithCategories> =
        types.value.sortedBy { it.order }.map { type ->
            TypeWithCategories(
                type = type,
                categories = categories.value.filter { it.parentTypeId == type.id }
            )
        }

    override suspend fun upsertType(type: TypeRoomEntity) {
        types.update { list -> list.filterNot { it.id == type.id } + type }
    }

    override suspend fun insertCategories(categories: List<CategoryRoomEntity>) {
        this.categories.update { list -> list + categories }
    }

    override suspend fun deleteCategoriesByParentId(parentTypeId: String) {
        categories.update { list -> list.filterNot { it.parentTypeId == parentTypeId } }
    }

    override suspend fun deleteTypeById(id: String) {
        types.update { list -> list.filterNot { it.id == id } }
        deleteCategoriesByParentId(id)
    }
}

class TypeRepositoryImplTest {

    @Test
    fun getTypes_returnsCategoriesSortedByOrder() = runTest {
        val dao = FakeTypeDao()
        val repo = TypeRepositoryImpl(dao)

        repo.insert(
            Type(
                id = "t1",
                typeIdTimestamp = 10010L,
                name = "Food",
                colorArgb = -1,
                order = 0,
                categories = listOf(
                    Category(id = 2, name = "Drink", order = 2, typeId = 10010L),
                    Category(id = 1, name = "Meal", order = 0, typeId = 10010L)
                )
            )
        )

        val result = repo.getTypes().first()
        assertEquals(1, result.size)
        assertEquals(listOf("Meal", "Drink"), result.first().categories.map { it.name })
    }

    @Test
    fun restore_mergesNewCategoriesIntoExistingType_withoutDuplicates() = runTest {
        val dao = FakeTypeDao()
        val repo = TypeRepositoryImpl(dao)

        val existingCat = Category(id = 1, name = "Meal", order = 0, typeId = 10010L)
        repo.insert(
            Type(
                id = "t1",
                typeIdTimestamp = 10010L,
                name = "Food",
                colorArgb = -1,
                order = 0,
                categories = listOf(existingCat)
            )
        )

        repo.restore(
            listOf(
                Type(
                    id = "t_backup",
                    typeIdTimestamp = 10010L,
                    name = "Food",
                    colorArgb = -1,
                    order = 0,
                    categories = listOf(
                        existingCat,
                        Category(id = 2, name = "Snack", order = 1, typeId = 10010L)
                    )
                )
            )
        )

        val result = repo.getTypes().first()
        assertEquals(1, result.size)
        assertEquals(listOf("Meal", "Snack"), result.first().categories.map { it.name })
    }
}
