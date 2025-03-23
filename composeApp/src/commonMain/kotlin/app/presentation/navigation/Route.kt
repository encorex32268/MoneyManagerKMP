package app.presentation.navigation

import core.domain.model.Expense
import core.domain.model.Type
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
sealed class Route(val route: String){

    @Serializable
    data object HomeGraph: Route("HomeGraph")

    @Serializable
    data object Home: Route("Home")

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
    data object ChartGraph: Route("ChartGraph")


    @Serializable
    data object Chart: Route("Chart")

    @Serializable
    data class  ChartDetail(
        val items: List<Expense>,
        val type: Type
    )

    @Serializable
    data object AnalyticsGraph: Route("AnalyticsGraph")

    @Serializable
    data object Analytics: Route("Analytics")

    @Serializable
    data object Backup

}



