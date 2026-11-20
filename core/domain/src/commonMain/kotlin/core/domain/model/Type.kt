package core.domain.model

import UUID
import kotlinx.serialization.Serializable

/**
 *  @param id Unique identifier string (UUID or migrated Realm ObjectId hex)
 *  @param typeIdHex Hex/String identifier
 *  @param typeIdTimestamp 10010
 *  @param name Type's name ; 食
 *  @param colorArgb Int -> -1710479
 *  @param order 0 -> Index
 *  @param isShow
 *  @param categories List<Category>
 */
@Serializable
data class Type(
    val id: String = UUID().generateUUID(),
    val typeIdHex: String = id,
    val typeIdTimestamp: Long,
    val name: String,
    val colorArgb: Int,
    val order: Int,
    val isShow: Boolean = true,
    val categories: List<Category> = emptyList()
)
