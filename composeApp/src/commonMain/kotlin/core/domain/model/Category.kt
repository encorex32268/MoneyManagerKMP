package core.domain.model

import kotlinx.serialization.Serializable


@Serializable
data class Category(
    var id: Int,
    var name: String="",
    var order: Int?=null,
    var typeId: Long?=null
)