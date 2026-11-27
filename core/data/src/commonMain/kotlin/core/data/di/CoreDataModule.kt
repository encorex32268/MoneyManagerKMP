package core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import core.data.DataStoreKeySettings
import core.data.database.MoneyManagerDatabase
import core.data.database.buildMoneyManagerDatabase
import core.data.datastore.DataStoreFactory
import core.data.repository.ExpenseRepositoryImpl
import core.data.repository.SpendingLimitRepositoryImpl
import core.data.repository.TypeRepositoryImpl
import core.domain.KeySettings
import core.domain.repository.ExpenseRepository
import core.domain.repository.SpendingLimitRepository
import core.domain.repository.TypeRepository
import org.koin.core.module.Module
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

expect val coreDataPlatformModule: Module

val coreDataModule = module {
    includes(coreDataPlatformModule)

    single<MoneyManagerDatabase> {
        buildMoneyManagerDatabase(get())
    }
    single { get<MoneyManagerDatabase>().expenseDao() }
    single { get<MoneyManagerDatabase>().typeDao() }
    single { get<MoneyManagerDatabase>().spendingLimitDao() }

    single<DataStore<Preferences>> {
        get<DataStoreFactory>().create()
    }
    single<KeySettings> {
        DataStoreKeySettings(dataStore = get())
    }

    singleOf(::ExpenseRepositoryImpl) { bind<ExpenseRepository>() }
    singleOf(::TypeRepositoryImpl) { bind<TypeRepository>() }
    singleOf(::SpendingLimitRepositoryImpl) { bind<SpendingLimitRepository>() }
}
