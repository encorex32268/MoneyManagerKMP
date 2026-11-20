package feature.analytics.di

import feature.analytics.data.AnalyticsRepositoryImpl
import feature.analytics.data.BackupRepositoryImpl
import feature.analytics.domain.AnalyticsRepository
import feature.analytics.domain.BackupRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val analyticsDataModule = module {
    singleOf(::AnalyticsRepositoryImpl) { bind<AnalyticsRepository>() }
    singleOf(::BackupRepositoryImpl) { bind<BackupRepository>() }
}
