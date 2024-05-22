package feature.edit.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.core.data.MongoDB
import feature.core.domain.model.Expense
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditExpenseScreenModel(
    private val mongoDB: MongoDB
): ScreenModel {

    private val _state = MutableStateFlow<Expense?>(null)
    val state = _state.asStateFlow()

    fun onEvent(event: EditExpenseEvent){
        when(event){
            is EditExpenseEvent.GetExpense -> {
                _state.update {
                    mongoDB.getExpense(event.expense)
                }
            }
            is EditExpenseEvent.DeleteExpense -> {
                screenModelScope.launch {
                    mongoDB.deleteExpense(event.expense)
                }
            }

        }
    }
}