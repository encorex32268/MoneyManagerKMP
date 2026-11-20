package feature.chart.presentation.navigation

import core.domain.model.Expense
import core.domain.model.Type
import kotlinx.serialization.Serializable

object ChartRoutes {
    const val GRAPH = "ChartGraph"
    const val CHART = "Chart"

    @Serializable
    data class ChartDetail(
        val items: List<Expense>,
        val type: Type
    )
}
