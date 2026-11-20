package feature.analytics.presentation.navigation

import kotlinx.serialization.Serializable

object AnalyticsRoutes {
    const val GRAPH = "AnalyticsGraph"
    const val ANALYTICS = "Analytics"

    @Serializable
    data object Backup
}
