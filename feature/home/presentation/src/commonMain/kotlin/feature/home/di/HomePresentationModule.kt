package feature.home.di

import core.domain.model.Expense
import feature.home.presentation.HomeViewModel
import feature.home.presentation.add.AddViewModel
import feature.home.presentation.add.type.TypeUi
import feature.home.presentation.add.type.TypeViewModel
import feature.home.presentation.add.type.category.TypeCategoryEditViewModel
import feature.home.presentation.edit.EditExpenseViewModel
import org.koin.compose.viewmodel.dsl.viewModel
import org.koin.compose.viewmodel.dsl.viewModelOf
import org.koin.core.parameter.parametersOf
import org.koin.dsl.module

val homePresentationModule = module {
    viewModelOf(::HomeViewModel)
    viewModel { (expense: Expense) ->
        AddViewModel(
            expense = expense,
            repository = get(),
            typeRepository = get()
        )
    }
    viewModel { (expense: Expense) ->
        EditExpenseViewModel(
            expense = get { parametersOf(expense) },
            repository = get(),
            typeRepository = get()
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
