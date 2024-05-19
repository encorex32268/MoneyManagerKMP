package feature.di

import feature.add.presentation.AddScreenModel
import feature.core.data.MongoDB
import feature.home.presentation.HomeScreenModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

val appModule = module {
    single{
        MongoDB()
    }
    factory{
        HomeScreenModel(
            mogoDB = get()
        )
    }
    factory {
        AddScreenModel(
            mogoDB = get()
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
