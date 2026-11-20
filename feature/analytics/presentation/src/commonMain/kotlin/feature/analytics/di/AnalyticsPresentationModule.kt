package feature.analytics.di

import feature.analytics.presentation.AnalyticsViewModel
import feature.analytics.presentation.backup.BackupViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val analyticsPresentationModule = module {
    viewModelOf(::AnalyticsViewModel)
    viewModelOf(::BackupViewModel)
}
