package core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.mongodb.kbson.ObjectId

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
    val content: String = ""
){
    constructor(): this(categoryId = 0 , typeId = 0L , description = "")
}
