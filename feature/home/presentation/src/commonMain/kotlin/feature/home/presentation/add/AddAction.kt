package feature.home.presentation.add

import core.domain.model.Expense
import core.presentation.model.CategoryUi

sealed interface AddAction {

    data class OnCostChange(val text: String): AddAction
    data class OnDescriptionChange(val description: String): AddAction
    data object OnDeleteTextClick: AddAction
    data object OnSaveClick: AddAction
    data class OnItemSelected(
        val categoryUi: CategoryUi?,
        val description : String = "",
        val isRecently: Boolean = false
    ) : AddAction

    data class  OnSelectedDate(val timestamp: Long): AddAction
    data object OnBack: AddAction
    data object OnGoToCategoryEditClick: AddAction
    data object CreateDefaultType: AddAction
}