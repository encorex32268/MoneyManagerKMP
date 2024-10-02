import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.home.add.type.TypeUi
import kotlinx.serialization.Serializable


sealed interface Route  {
    @Serializable
    data object Home

    @Serializable
    data class  HomeAdd(
        val expense: Expense,
        val isAddNew: Boolean
    )
    @Serializable
    data object Types

    @Serializable
    data class TypeCategoryEdit(
        val type: Type
    )

    @Serializable
    data class  HomeEdit(
        val expense: Expense
    )
    @Serializable
    data object Chart

    @Serializable
    data class  ChartDetail(
        val items: List<Expense>
    )
}



