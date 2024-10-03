package feature.di

import AppViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.russhwolf.settings.Settings
import feature.chart.ChartViewModel
import feature.chart.chartdetail.DetailViewModel
import feature.core.data.DefaultKeySettings
import feature.core.data.RealmFactory
import feature.core.data.repository.ExpenseRepositoryImpl
import feature.core.data.repository.TypeRepositoryImpl
import feature.core.domain.KeySettings
import feature.core.domain.model.Expense
import feature.core.domain.repository.ExpenseRepository
import feature.core.domain.repository.TypeRepository
import feature.home.HomeViewModel
import feature.home.add.AddViewModel
import feature.home.add.type.TypeUi
import feature.home.add.type.TypeViewModel
import feature.home.add.type.category.TypeCategoryEditViewModel
import feature.home.edit.EditExpenseViewModel
import io.realm.kotlin.Realm
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module


val appModule = module {
    single<Realm> {
        RealmFactory().build()
    }

    single {
        DefaultKeySettings(settings = Settings())
    }.bind<KeySettings>()

    singleOf(::ExpenseRepositoryImpl).bind<ExpenseRepository>()
    singleOf(::TypeRepositoryImpl).bind<TypeRepository>()

    viewModelOf(::AppViewModel)

    viewModelOf(::HomeViewModel)
    viewModel { (expense: Expense) ->
        AddViewModel(
            expense = expense,
            repository = get(),
            typeRepository = get()
        )
    }
    viewModelOf(::EditExpenseViewModel)
    viewModelOf(::ChartViewModel)
    viewModelOf(::DetailViewModel)
    viewModelOf(::TypeViewModel)
    viewModel { (typeUi: TypeUi) ->
        TypeCategoryEditViewModel(
            typeUi = get { parametersOf(typeUi) },
            repository = get()
        )
    }


}

