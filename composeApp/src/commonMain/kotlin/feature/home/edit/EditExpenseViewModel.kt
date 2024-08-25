package feature.home.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.data.MongoDB
import feature.core.domain.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditExpenseViewModel(
    private val mongoDB: MongoDB
): ViewModel() {

    private val _state = MutableStateFlow(EditExpenseState())
    val state = _state.asStateFlow()

    fun onEvent(event: EditExpenseEvent){
        when(event){
            is EditExpenseEvent.GetExpense    -> {
                _state.update {
                    it.copy(
                        currentExpense = mongoDB.getExpense(event.expense)
                    )
                }
            }
            is EditExpenseEvent.DeleteExpense -> {
                viewModelScope.launch {
                    mongoDB.deleteExpense(event.expense)
                }
            }
            else -> Unit
        }
    }
}