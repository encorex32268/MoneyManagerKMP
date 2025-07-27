package feature.home.presentation.model

import core.domain.model.Type
import org.mongodb.kbson.ObjectId

data class ExpenseUi(
    val id: ObjectId = ObjectId(),
    val categoryId: Int,
    val typeId: Long,
    val description: String,
    val type: Type? = null,
    val cost: Long = 0,
    val timestamp: Long = 0,
    val content: String = "",
    val colorRGB: Int? = null
)
