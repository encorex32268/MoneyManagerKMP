package feature.di

import feature.core.data.MongoDB
import feature.chart.ChartViewModel
import feature.chart.chartdetail.DetailViewModel
import feature.home.HomeViewModel
import feature.home.add.AddViewModel
import feature.home.edit.EditExpenseViewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val appModule = module {
    singleOf(::MongoDB)
    viewModelOf(::HomeViewModel)
    viewModelOf(::AddViewModel)
    viewModelOf(::EditExpenseViewModel)
    viewModelOf(::ChartViewModel)
    viewModelOf(::DetailViewModel)
}

