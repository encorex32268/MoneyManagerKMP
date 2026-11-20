package core.data.di

import com.russhwolf.settings.Settings
import core.data.DefaultKeySettings
import core.data.RealmFactory
import core.data.repository.ExpenseRepositoryImpl
import core.data.repository.SpendingLimitRepositoryImpl
import core.data.repository.TypeRepositoryImpl
import core.domain.KeySettings
import core.domain.repository.ExpenseRepository
import core.domain.repository.SpendingLimitRepository
import core.domain.repository.TypeRepository
import io.realm.kotlin.Realm
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val coreDataModule = module {
    single<Realm> {
        RealmFactory().build()
    }
    single<KeySettings> {
        DefaultKeySettings(settings = Settings())
    }
    singleOf(::ExpenseRepositoryImpl) { bind<ExpenseRepository>() }
    singleOf(::TypeRepositoryImpl) { bind<TypeRepository>() }
    singleOf(::SpendingLimitRepositoryImpl) { bind<SpendingLimitRepository>() }
}
