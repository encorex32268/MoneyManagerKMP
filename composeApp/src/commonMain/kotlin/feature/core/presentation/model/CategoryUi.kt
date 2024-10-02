package feature.core.presentation.model


data class CategoryUi(
    val id: Int,
    val name: String,
    val order: Int,
    var typeId: Long?=null,
    val isClick: Boolean,
    val colorArgb: Int? = null
)
