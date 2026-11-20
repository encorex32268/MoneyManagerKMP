package feature.chart.di

import core.domain.model.Expense
import core.domain.model.Type
import feature.chart.presentation.ChartViewModel
import feature.chart.presentation.chartdetail.DetailViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val chartPresentationModule = module {
    viewModelOf(::ChartViewModel)
    viewModel { (items: List<Expense>, type: Type) ->
        DetailViewModel(
            type = type,
            items = items
        )
    }
}
