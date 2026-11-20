package feature.home.presentation.add.type

import UUID
import core.presentation.model.CategoryUi

data class TypeUi(
    val id: String = UUID().generateUUID(),
    val typeIdHex: String = id,
    val typeIdTimestamp: Long,
    val name: String,
    val colorArgb: Int,
    val order: Int,
    val isShow: Boolean = true,
    val categories: List<CategoryUi> = emptyList()
)
