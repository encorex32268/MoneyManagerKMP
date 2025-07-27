package feature.home.presentation.edit

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.model.Expense
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import feature.home.domain.mapper.toExpense
import feature.home.domain.mapper.toExpenseUi
import feature.home.presentation.model.ExpenseUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EditExpenseViewModel(
    private val expense: Expense,
    private val repository: ExpenseRepository,
    private val typeRepository: TypeRepository
): ViewModel() {

    private var hasInitialLoadedData = false

    private val _state = MutableStateFlow(EditExpenseState())
    val state = _state
        .onStart {
            if (!hasInitialLoadedData){
                initData()
                hasInitialLoadedData = true
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )


    private val _uiEvent = Channel<EditExpenseUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private var currentText = expense.content

    fun onEvent(event: EditExpenseEvent){
        when(event){
            EditExpenseEvent.OnDelete -> onDelete()
            is EditExpenseEvent.OnContentChange -> onContentChange(event.text)
            EditExpenseEvent.OnBackClick -> onBackClick()
            EditExpenseEvent.OnGoAddScreenClick -> onGoAddScreenClick()
            EditExpenseEvent.OnSaveClick -> onSaveClick()
        }
    }

    private fun initData(){
        viewModelScope.launch {
            val dbExpense = repository.getExpense(expense).firstOrNull()
            if (dbExpense == null) return@launch
            val types = typeRepository.getTypes().firstOrNull()?:emptyList()
            var expenseUi = dbExpense.toExpenseUi()

            //Find Background Color by TypeId
            if (types.isNotEmpty()){
                val currentType = types.find { it.typeIdTimestamp == dbExpense.typeId }
                currentType?.let {
                    expenseUi = expenseUi.copy(
                        colorRGB = it.colorArgb
                    )
                }
            }
            _state.update {
                it.copy(
                    currentExpenseUi = expenseUi
                )
            }
        }


    }

    private fun onSaveClick() {
        val currentState = _state.value
        val expenseUi = currentState.currentExpenseUi
        updateExpense(
            expenseUi = expenseUi,
            onUpdateDone = {
                currentText = expenseUi?.content?:currentText
                _state.update {
                    it.copy(
                        isShowSaveIcon = false
                    )
                }
                viewModelScope.launch {
                    _uiEvent.send(EditExpenseUiEvent.HideKeyboard)
                }
            }
        )

    }

    private fun onGoAddScreenClick() {
        val currentState = _state.value
        val expenseUi = currentState.currentExpenseUi
        updateExpense(
            expenseUi = expenseUi,
            onUpdateDone = {
                viewModelScope.launch {
                    _uiEvent.send(EditExpenseUiEvent.OnGoAddScreen(expense))
                }
            }
        )
    }

    private fun onBackClick() {
        val currentState = _state.value
        updateExpense(
            expenseUi = currentState.currentExpenseUi,
            onUpdateDone = {
                viewModelScope.launch {
                    _uiEvent.send(EditExpenseUiEvent.OnBack)
                }
            }
        )
    }

    private fun onContentChange(text: String) {
        val currentState = _state.value
        val contentChangExpense = currentState.currentExpenseUi?.copy(
            content = text
        )
        _state.update {
            it.copy(
                currentExpenseUi = contentChangExpense,
                isShowSaveIcon = currentText != text
            )
        }
    }

    private fun onDelete() {
        val currentExpenseUi = _state.value.currentExpenseUi
        currentExpenseUi?.let { expenseUi ->
            viewModelScope.launch {
                repository.delete(expense = expenseUi.toExpense())
                _uiEvent.send(EditExpenseUiEvent.OnBack)
            }
        }?:return
    }


    private fun updateExpense(
        expenseUi: ExpenseUi?,
        onUpdateDone: () -> Unit = {}
    ){
        if (expenseUi == null) return
        viewModelScope.launch {
            if (currentText != expenseUi.content){
                repository.update(
                    expense = expenseUi.toExpense()
                )
            }
            onUpdateDone()
        }
    }
}