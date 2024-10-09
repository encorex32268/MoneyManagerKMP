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
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.internal.ChannelFlow
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

    private val _uiEvent = Channel<EditExpenseUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

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
                val currentExpense = state.value.currentExpense
                currentExpense?.let {
                    _state.update {
                        it.copy(
                            currentExpense = currentExpense.copy(
                                content = event.text
                            )
                        )
                    }
                }?:return
            }

            EditExpenseEvent.OnBackClick        -> {
                viewModelScope.launch {
                    state.value.currentExpense?.let {
                        repository.update(
                            expense = it
                        )
                    }
                    _uiEvent.send(EditExpenseUiEvent.OnBack)
                }
            }
        }
    }
}