package feature.home.presentation.model

import UUID
import core.domain.model.Type

data class ExpenseUi(
    val id: String = UUID().generateUUID(),
    val categoryId: Int,
    val typeId: Long,
    val description: String,
    val type: Type? = null,
    val cost: Long = 0,
    val timestamp: Long = 0,
    val content: String = "",
    val colorRGB: Int? = null
)
