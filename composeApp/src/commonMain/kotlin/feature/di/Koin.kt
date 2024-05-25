package feature.di

import feature.add.presentation.AddScreenModel
import feature.chart.presentation.ChartScreenModel
import feature.core.data.MongoDB
import feature.edit.presentation.EditExpenseScreenModel
import feature.home.presentation.HomeScreenModel
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
}

fun initKoin() {
    startKoin {
        modules(
            appModule
        )
    }
}
