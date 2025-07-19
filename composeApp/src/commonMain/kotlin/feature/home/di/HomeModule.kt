package feature.home.di

import core.domain.model.Expense
import feature.home.data.repository.HomeRepositoryImpl
import feature.home.domain.repository.HomeRepository
import feature.home.presentation.HomeViewModel
import feature.home.presentation.add.AddViewModel
import feature.home.presentation.add.type.TypeUi
import feature.home.presentation.add.type.TypeViewModel
import feature.home.presentation.add.type.category.TypeCategoryEditViewModel
import feature.home.presentation.edit.EditExpenseViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.singleOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.bind
import org.koin.dsl.module

val homeModule = module {
    singleOf(::HomeRepositoryImpl).bind<HomeRepository>()
    viewModelOf(::HomeViewModel)
    viewModel { (expense: Expense) ->
        AddViewModel(
            expense = expense,
            repository = get(),
            typeRepository = get(),
            keySettings = get()
        )
    }

    viewModel{ (expense: Expense) ->
        EditExpenseViewModel(
            expense = get { parametersOf(expense) },
            repository = get()
        )
    }
    viewModelOf(::TypeViewModel)
    viewModel { (typeUi: TypeUi) ->
        TypeCategoryEditViewModel(
            typeUi = get { parametersOf(typeUi) },
            repository = get()
        )
    }

}