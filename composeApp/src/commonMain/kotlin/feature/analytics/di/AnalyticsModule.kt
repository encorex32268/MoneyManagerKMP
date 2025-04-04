package feature.analytics.di

import feature.analytics.data.AnalyticsRepositoryImpl
import feature.analytics.domain.AnalyticsRepository
import feature.analytics.presentation.AnalyticsViewModel
import feature.analytics.presentation.backup.BackupViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val analyticsModule = module {

    singleOf(::AnalyticsRepositoryImpl).bind<AnalyticsRepository>()


    viewModelOf(::AnalyticsViewModel)
    viewModelOf(::BackupViewModel)
}