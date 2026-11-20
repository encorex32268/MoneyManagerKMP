package core.domain.model

import UUID
import kotlinx.serialization.Serializable

/**
 * year | month | limit
 * 2024 | 12 | 20,000
 * 2025 | 1 | 30,000
 * ....
 */
@Serializable
data class SpendingLimit(
    val id: String = UUID().generateUUID(),
    val year: Int,
    val month: Int,
    val limit: Long
)
