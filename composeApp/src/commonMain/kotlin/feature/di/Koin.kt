package feature.di

import app.presentation.AppViewModel
import com.russhwolf.settings.Settings
import feature.analytics.di.analyticsModule
import feature.chart.di.chartModule
import core.data.DefaultKeySettings
import core.data.RealmFactory
import core.data.repository.ExpenseRepositoryImpl
import core.data.repository.SpendingLimitRepositoryImpl
import core.data.repository.TypeRepositoryImpl
import core.domain.KeySettings
import core.domain.repository.ExpenseRepository
import core.domain.repository.SpendingLimitRepository
import core.domain.repository.TypeRepository
import feature.home.di.homeModule
import io.realm.kotlin.Realm
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module


private val appModule = module {
    single<Realm> {
        RealmFactory().build()
    }

    single {
        DefaultKeySettings(settings = Settings())
    }.bind<KeySettings>()

    singleOf(::ExpenseRepositoryImpl).bind<ExpenseRepository>()
    singleOf(::TypeRepositoryImpl).bind<TypeRepository>()
    singleOf(::SpendingLimitRepositoryImpl).bind<SpendingLimitRepository>()

    viewModelOf(::AppViewModel)

}

val sharedModules = listOf(
    appModule,
    homeModule,
    chartModule,
    analyticsModule,
    appModule
)

