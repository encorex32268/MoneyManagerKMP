package feature.home.presentation.add

import core.domain.model.Expense
import core.presentation.model.CategoryUi

sealed interface AddEvent {

    data class OnCostChange(val text: String): AddEvent
    data class OnDescriptionChange(val description: String): AddEvent
    data object OnDeleteTextClick: AddEvent
    data object OnSaveClick: AddEvent
    data class OnItemSelected(
        val categoryUi: CategoryUi?,
        val description : String = "",
        val isRecently: Boolean = false
    ) : AddEvent

    data class  OnSelectedDate(val timestamp: Long): AddEvent
    data object OnBack: AddEvent
    data object OnGoToCategoryEditClick: AddEvent
    data object CreateDefaultType: AddEvent
}