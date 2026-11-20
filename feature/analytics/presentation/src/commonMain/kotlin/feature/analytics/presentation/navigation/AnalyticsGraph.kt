package feature.analytics.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import feature.analytics.presentation.AnalyticsScreenRoot
import feature.analytics.presentation.backup.BackupScreenRoot

fun NavGraphBuilder.analyticsGraph(
    navController: NavHostController,
    onDarkLightModeSwitch: () -> Unit = {}
) {
    navigation(
        route = AnalyticsRoutes.GRAPH,
        startDestination = AnalyticsRoutes.ANALYTICS
    ){
        composable(AnalyticsRoutes.ANALYTICS) {
            AnalyticsScreenRoot(
                onBackupClick = {
                    navController.navigate(
                        AnalyticsRoutes.Backup
                    )
                },
                onDarkLightModeSwitch = onDarkLightModeSwitch
            )
        }
        composable<AnalyticsRoutes.Backup> {
            BackupScreenRoot(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }

}
