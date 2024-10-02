package feature.home.add.type

import feature.core.presentation.model.CategoryUi
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

data class TypeUi(
    val id: ObjectId = BsonObjectId(),
    val typeIdHex: String = id.toHexString(),
    val typeIdTimestamp: Long,
    val name: String,
    val colorArgb: Int,
    val order: Int,
    val isShow: Boolean = true,
    val categories: List<CategoryUi> = emptyList()
)
