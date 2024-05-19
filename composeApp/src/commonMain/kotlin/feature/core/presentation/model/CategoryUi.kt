package feature.core.presentation.model

data class CategoryUi(
    val id: Int?=null,
    val categoryId: Int,
    val typeId: Int,
    val isClick: Boolean,
    val name: String?=null
)
