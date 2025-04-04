package core.domain.model

import kotlinx.serialization.Serializable
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Serializable
data class Type(
    val id: ObjectId = BsonObjectId(),
    val typeIdHex: String = id.toHexString(),
    val typeIdTimestamp: Long,
    val name: String,
    val colorArgb: Int,
    val order: Int,
    val isShow: Boolean = true,
    val categories: List<Category> = emptyList()
)
