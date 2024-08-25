package feature.core.domain.model

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.mongodb.kbson.ObjectId

@Serializable
data class Expense(
    @Transient val id: ObjectId = ObjectId(),
    val idString: String = id.toHexString(),
    val categoryId: Int,
    val typeId: Int,
    val description: String,
    val isIncome: Boolean=false,
    val cost: Long = 0,
    val timestamp: Long = 0
){
    constructor(): this(categoryId = 0 , typeId = 0 , description = "")
}
