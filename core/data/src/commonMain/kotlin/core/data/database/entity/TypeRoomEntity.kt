package core.data.database.entity

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "types")
data class TypeRoomEntity(
    @PrimaryKey
    val id: String,
    val typeIdHex: String,
    val typeIdTimestamp: Long,
    val name: String,
    val colorArgb: Int,
    val order: Int,
    val isShow: Boolean
)

@Entity(
    tableName = "categories",
    foreignKeys = [
        ForeignKey(
            entity = TypeRoomEntity::class,
            parentColumns = ["id"],
            childColumns = ["parentTypeId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["parentTypeId"])]
)
data class CategoryRoomEntity(
    @PrimaryKey(autoGenerate = true)
    val rowId: Long = 0,
    val id: Int,
    val name: String,
    val order: Int,
    val typeId: Long,
    val parentTypeId: String
)

data class TypeWithCategories(
    @Embedded
    val type: TypeRoomEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "parentTypeId"
    )
    val categories: List<CategoryRoomEntity>
)
