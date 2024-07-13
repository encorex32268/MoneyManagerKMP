package feature.di

import feature.presentation.add.AddScreenModel
import feature.presentation.chart.ChartScreenModel
import feature.presentation.chart.chartdetail.ChartDetailScreenModel
import feature.core.data.MongoDB
import feature.presentation.edit.EditExpenseScreenModel
import feature.presentation.home.HomeScreenModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single{
        MongoDB()
    }
    factory{
        HomeScreenModel(
            mongoDB = get()
        )
    }
    factory {
        AddScreenModel(
            mongoDB = get()
        )
    }
    factory {
        EditExpenseScreenModel(
            mongoDB = get()
        )
    }
    factory {
        ChartScreenModel(
            mongoDB = get()
        )
    }
    factory {
        ChartDetailScreenModel()
    }
}

fun initKoin() {
    startKoin {
        modules(
            appModule
        )
    }
}
