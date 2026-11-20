package feature.home.presentation.navigation

import core.domain.model.Expense
import core.domain.model.Type
import kotlinx.serialization.Serializable

object HomeRoutes {
    const val GRAPH = "HomeGraph"
    const val HOME = "Home"

    @Serializable
    data class HomeAdd(
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
    data class HomeEdit(
        val expense: Expense
    )
}
