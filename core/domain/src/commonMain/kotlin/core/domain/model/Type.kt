package core.domain.model

import kotlinx.serialization.Serializable
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

/**
 *  @param id from realm id  ; BsonObjectId(6768e6c4d8e433301828c624)
 *  @param typeIdHex idToHex ; 6768e6c4d8e433301828c624
 *  @param typeIdTimestamp 10010
 *  @param name Type's name ; 食
 *  @param colorArgb Int -> -1710479
 *  @param order 0 -> Index
 *  @param isShow
 *  @param categories List<Category> ; [ Category(id=10011, name=食物, order=0, typeId=10010), Category(id=10012, name=速食, order=1, typeId=10010) ... ]
 */
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
