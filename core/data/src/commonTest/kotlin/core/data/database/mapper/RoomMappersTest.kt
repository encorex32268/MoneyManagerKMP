package core.data.database.mapper

import core.data.database.entity.CategoryRoomEntity
import core.data.database.entity.TypeRoomEntity
import core.data.database.entity.TypeWithCategories
import core.domain.model.Category
import core.domain.model.Expense
import core.domain.model.Type
import kotlin.test.Test
import kotlin.test.assertEquals

class RoomMappersTest {

    @Test
    fun `Expense round-trip preserves fields and unicode description`() {
        val expense = Expense(
            id = "exp-1",
            idString = "exp-1",
            categoryId = 10010,
            typeId = 1L,
            description = "拉麵 🍜👨‍👩‍👧‍👦",
            isIncome = false,
            cost = 250L,
            timestamp = 1700000000000L,
            content = "備註\n換行"
        )

        val mappedBack = expense.toRoomEntity().toExpense()
        assertEquals(expense, mappedBack)
    }

    @Test
    fun `Category with null order and typeId defaults to 0 in RoomEntity`() {
        val category = Category(
            id = 101,
            name = "Coffee ☕",
            order = null,
            typeId = null
        )

        val entity = category.toRoomEntity(parentTypeId = "type-1")
        assertEquals(0, entity.order)
        assertEquals(0L, entity.typeId)
        assertEquals("type-1", entity.parentTypeId)
    }

    @Test
    fun `TypeWithCategories sorts categories by order ascending`() {
        val typeWithCategories = TypeWithCategories(
            type = TypeRoomEntity(
                id = "t1",
                typeIdHex = "hex",
                typeIdTimestamp = 1L,
                name = "Food",
                colorArgb = 0xFF0000,
                order = 0,
                isShow = true
            ),
            categories = listOf(
                CategoryRoomEntity(id = 3, name = "Dinner", order = 2, typeId = 1L, parentTypeId = "t1"),
                CategoryRoomEntity(id = 1, name = "Breakfast", order = 0, typeId = 1L, parentTypeId = "t1"),
                CategoryRoomEntity(id = 2, name = "Lunch", order = 1, typeId = 1L, parentTypeId = "t1")
            )
        )

        val domainType = typeWithCategories.toType()
        assertEquals(listOf(1, 2, 3), domainType.categories.map { it.id })
    }

    @Test
    fun `Type toRoomTypeAndCategories assigns parentTypeId to all child categories`() {
        val type = Type(
            id = "parent-42",
            typeIdTimestamp = 20010L,
            name = "Transport",
            colorArgb = -1710479,
            order = 0,
            categories = listOf(
                Category(id = 1, name = "MRT", order = 0, typeId = 20010L),
                Category(id = 2, name = "Bus", order = 1, typeId = 20010L)
            )
        )

        val (typeEntity, categoryEntities) = type.toRoomTypeAndCategories()
        assertEquals("parent-42", typeEntity.id)
        assertEquals(listOf("parent-42", "parent-42"), categoryEntities.map { it.parentTypeId })
    }
}
