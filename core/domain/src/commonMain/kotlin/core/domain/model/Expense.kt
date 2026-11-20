package core.domain.model

import UUID
import kotlinx.serialization.Serializable

/**
 * @param id Unique identifier string (UUID or migrated Realm ObjectId hex)
 * @param idString Alias of id for backward-compatible display/lookup
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
    val id: String = UUID().generateUUID(),
    val idString: String = id,
    val categoryId: Int,
    val typeId: Long,
    val description: String,
    val isIncome: Boolean = false,
    val cost: Long = 0,
    val timestamp: Long = 0,
    val content: String = "",
    val type: Type? = null
) {
    constructor() : this(categoryId = 0, typeId = 0L, description = "")
}
