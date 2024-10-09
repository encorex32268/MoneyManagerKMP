package feature.home.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import feature.core.domain.model.Expense
import feature.core.domain.repository.ExpenseRepository
import feature.core.domain.repository.TypeRepository
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.internal.ChannelFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditExpenseViewModel(
    private val expense: Expense,
    private val repository: ExpenseRepository,
    private val typeRepository: TypeRepository
): ViewModel() {

    init {
        viewModelScope.launch {
            val types = typeRepository.getTypes()
            val expenseById = repository.getExpense(expense)
            combine(types , expenseById){ dataTypes , dataExpense ->
                _state.update {
                    it.copy(
                        currentExpense = dataExpense,
                        typeItems = dataTypes
                    )
                }
            }.launchIn(this)
        }
    }
    private val _state = MutableStateFlow(
        EditExpenseState()
    )
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<EditExpenseUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val currentText = MutableStateFlow(expense.content)


    fun onEvent(event: EditExpenseEvent){
        when(event){
            EditExpenseEvent.OnDelete -> {
                state.value.currentExpense?.let {
                    viewModelScope.launch {
                        repository.delete(
                            expense = it
                        )
                        _uiEvent.send(EditExpenseUiEvent.OnBack)
                    }
                }?:return

            }

            is EditExpenseEvent.OnContentChange -> {
                _state.update {
                    it.copy(
                        currentExpense = state.value.currentExpense?.copy(
                            content = event.text
                        )
                    )
                }
            }

            EditExpenseEvent.OnBackClick        -> {
                viewModelScope.launch {
                    state.value.currentExpense?.let {
                        if (currentText.value != it.content){
                            repository.update(
                                expense = it
                            )
                        }
                    }
                    _uiEvent.send(EditExpenseUiEvent.OnBack)
                }
            }

            EditExpenseEvent.OnGoAddScreenClick -> {
                state.value.currentExpense?.let {
                    viewModelScope.launch {
                        if (currentText.value != it.content) {
                            repository.update(
                                expense = it
                            )
                        }
                        _uiEvent.send(EditExpenseUiEvent.OnGoAddScreen(it))
                    }
                }?:return

            }
        }
    }
}