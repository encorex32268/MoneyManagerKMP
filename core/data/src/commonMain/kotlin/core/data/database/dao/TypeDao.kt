package core.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import core.data.database.entity.CategoryRoomEntity
import core.data.database.entity.TypeRoomEntity
import core.data.database.entity.TypeWithCategories
import kotlinx.coroutines.flow.Flow

@Dao
interface TypeDao {

    @Transaction
    @Query("SELECT * FROM types ORDER BY `order` ASC")
    fun getTypes(): Flow<List<TypeWithCategories>>

    @Transaction
    @Query("SELECT * FROM types ORDER BY `order` ASC")
    suspend fun getTypesOnce(): List<TypeWithCategories>

    @Upsert
    suspend fun upsertType(type: TypeRoomEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<CategoryRoomEntity>)

    @Query("DELETE FROM categories WHERE parentTypeId = :parentTypeId")
    suspend fun deleteCategoriesByParentId(parentTypeId: String)

    @Query("DELETE FROM types WHERE id = :id")
    suspend fun deleteTypeById(id: String)

    @Transaction
    suspend fun upsertTypeWithCategories(
        type: TypeRoomEntity,
        categories: List<CategoryRoomEntity>
    ) {
        upsertType(type)
        deleteCategoriesByParentId(type.id)
        if (categories.isNotEmpty()) {
            insertCategories(categories)
        }
    }

    @Transaction
    suspend fun upsertAllTypesWithCategories(
        items: List<Pair<TypeRoomEntity, List<CategoryRoomEntity>>>
    ) {
        items.forEach { (type, categories) ->
            upsertTypeWithCategories(type, categories)
        }
    }
}
