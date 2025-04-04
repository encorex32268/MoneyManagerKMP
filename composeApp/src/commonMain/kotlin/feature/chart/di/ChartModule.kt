package feature.chart.di

import feature.chart.data.repository.ChartRepositoryImpl
import feature.chart.domain.repository.ChartRepository
import feature.chart.presentation.ChartViewModel
import feature.chart.presentation.chartdetail.DetailViewModel
import core.domain.model.Expense
import core.domain.model.Type
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val chartModule = module {
    singleOf(::ChartRepositoryImpl).bind<ChartRepository>()
    viewModelOf(::ChartViewModel)
    viewModel { (items: List<Expense>,type: Type) ->
        DetailViewModel(
            type = type,
            items = items
        )
    }
}