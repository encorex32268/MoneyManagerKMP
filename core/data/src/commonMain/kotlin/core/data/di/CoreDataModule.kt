package core.data.di

import com.russhwolf.settings.Settings
import core.data.DefaultKeySettings
import core.data.RealmFactory
import core.data.database.MoneyManagerDatabase
import core.data.database.buildMoneyManagerDatabase
import core.data.migration.RealmToRoomMigrator
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

    single { RealmFactory() }
    singleOf(::RealmToRoomMigrator)

    single<KeySettings> {
        DefaultKeySettings(settings = Settings())
    }

    singleOf(::ExpenseRepositoryImpl) { bind<ExpenseRepository>() }
    singleOf(::TypeRepositoryImpl) { bind<TypeRepository>() }
    singleOf(::SpendingLimitRepositoryImpl) { bind<SpendingLimitRepository>() }
}
