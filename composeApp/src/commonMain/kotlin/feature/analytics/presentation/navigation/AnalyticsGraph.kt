package feature.analytics.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import app.presentation.navigation.Route
import feature.analytics.presentation.AnalyticsScreenRoot
import feature.analytics.presentation.backup.BackupScreenRoot

fun NavGraphBuilder.analyticsGraph(
    navController: NavHostController,
    onDarkLightModeSwitch: () -> Unit = {}
) {
    navigation(
        route = Route.AnalyticsGraph.route,
        startDestination = Route.Analytics.route
    ){
        composable(Route.Analytics.route) {
            AnalyticsScreenRoot(
                onBackupClick = {
                    navController.navigate(
                        Route.Backup
                    )
                },
                onDarkLightModeSwitch = onDarkLightModeSwitch
            )
        }
        composable<Route.Backup> {
            BackupScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }

}
