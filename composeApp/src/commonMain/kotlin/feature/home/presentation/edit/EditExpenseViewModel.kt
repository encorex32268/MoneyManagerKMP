package feature.home.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.domain.model.Expense
import feature.core.domain.repository.ExpenseRepository
import feature.core.domain.repository.TypeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditExpenseViewModel(
    private val expense: Expense,
    private val repository: ExpenseRepository,
    private val typeRepository: TypeRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            typeRepository.getTypes().collectLatest { data ->
                _state.update {
                    it.copy(
                        typeItems = data,
                        currentExpense = expense
                    )
                }
            }
        }
    }
    private val _state = MutableStateFlow(
        EditExpenseState()
    )
    val state = _state.asStateFlow()

    fun onEvent(event: EditExpenseEvent){
        when(event){
            EditExpenseEvent.OnDelete -> {
                if(state.value.currentExpense == null) return
                viewModelScope.launch {
                    repository.delete(
                        expense = state.value.currentExpense!!
                    )
                }
            }
        }
    }
}