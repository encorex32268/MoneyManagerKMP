package core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.mongodb.kbson.ObjectId

/**
 * @param id Realm BsonObjectId(6844e5e8c578e54812d47d62)
 * @param idString 6844e5e8c578e54812d47d62
 * @param categoryId 10024
 * @param typeId 10010 -> food ID
 * @param description 餅乾
 * @param isIncome Income 收入  false -> 支出
 * @param cost  500
 * @param timestamp 1749345768941
 * @param content note
 */
@Serializable
data class Expense(
    val id: ObjectId = ObjectId(),
    val idString: String = id.toHexString(),
    val categoryId: Int,
    val typeId: Long,
    val description: String,
    val isIncome: Boolean=false,
    val cost: Long = 0,
    val timestamp: Long = 0,
    val content: String = "",
    val type: Type? = null
){
    constructor(): this(categoryId = 0 , typeId = 0L , description = "")


}
