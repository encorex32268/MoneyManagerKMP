package core.domain.model

import kotlinx.serialization.Serializable
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

/**
 * year | month | limit
 * 2024 | 12 | 20,000
 * 2025 | 1 | 30,000
 * ....
 */
@Serializable
data class SpendingLimit(
    val id: ObjectId = BsonObjectId(),
    val year: Int,
    val month:Int,
    val limit: Long
)

