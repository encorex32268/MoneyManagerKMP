package app.di

import app.presentation.AppViewModel
import core.data.di.coreDataModule
import feature.analytics.di.analyticsDataModule
import feature.analytics.di.analyticsPresentationModule
import feature.chart.di.chartDataModule
import feature.chart.di.chartPresentationModule
import feature.home.di.homeDataModule
import feature.home.di.homePresentationModule
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module

private val appModule = module {
    single<CoroutineScope> {
        CoroutineScope(SupervisorJob())
    }
    viewModelOf(::AppViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            coreDataModule,
            homeDataModule,
            homePresentationModule,
            chartDataModule,
            chartPresentationModule,
            analyticsDataModule,
            analyticsPresentationModule,
            appModule
        )
    }
}
