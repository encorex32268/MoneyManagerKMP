package feature.home.di

import feature.home.data.repository.HomeRepositoryImpl
import feature.home.domain.repository.HomeRepository
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val homeDataModule = module {
    singleOf(::HomeRepositoryImpl) { bind<HomeRepository>() }
}
