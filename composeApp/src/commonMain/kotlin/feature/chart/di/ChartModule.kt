package feature.chart.di

import feature.chart.data.repository.ChartRepositoryImpl
import feature.chart.domain.repository.ChartRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val chartModule = module {
    singleOf(::ChartRepositoryImpl).bind<ChartRepository>()
}