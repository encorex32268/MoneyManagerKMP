package feature.add.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.core.data.MongoDB
import feature.core.domain.mapper.toCategory
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.date.DateConverter
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddScreenModel(
    private val mogoDB: MongoDB
): ScreenModel {

    companion object{
        private const val COST_MAX_LENGTH = 10
    }

    private val _state = MutableStateFlow(AddState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<AddUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        val localDateTime = DateConverter.getNowDate()
        _state.update {
            it.copy(
                year = localDateTime.year,
                monthNumber = localDateTime.monthNumber,
                dayOfMonth = localDateTime.dayOfMonth,
                nowLocalDateTime = localDateTime
            )
        }
    }

    fun onEvent(event: AddEvent){
        when(event){
            is AddEvent.SetupExpense -> {
                event.expense?.let { expense ->
                    _state.update {
                        it.copy(
                            currentExpense = expense
                        )
                    }
                }
            }
            is AddEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
            is AddEvent.OnCostChange -> {
                val currentCost = state.value.cost
                if (currentCost.length > COST_MAX_LENGTH) return
                _state.update {
                    it.copy(
                        cost = currentCost + event.text
                    )
                }
            }
            AddEvent.OnDeleteTextClick -> {
                val currentCost = state.value.cost
                if (currentCost.trim().isEmpty()) return
                _state.update {
                    it.copy(
                        cost = currentCost.dropLast(1)
                    )
                }
            }
            is AddEvent.OnTypeChange -> {
                _state.update {
                    it.copy(
                        isIncome = event.isClicked
                    )
                }
            }
            is AddEvent.OnSelectedDate -> {
                val localDateTime = DateConverter.getLocalDateTimeFromTimestamp(event.timestamp)
                _state.update {
                    it.copy(
                        year = localDateTime.year,
                        monthNumber = localDateTime.monthNumber,
                        dayOfMonth = localDateTime.dayOfMonth
                    )
                }
            }
            is AddEvent.OnItemSelected -> {
                if(event.categoryUi.typeId == CategoryList.RECENTLY){
                    _state.update {
                        it.copy(
                            categoryItems = it.categoryItems.map {
                                it.copy(isClick = false)
                            },
                            recentlyCategoryItems = it.recentlyCategoryItems.map {
                                it.copy(isClick = it.categoryId == event.categoryUi.categoryId)
                            }
                        )
                    }
                }else{
                    _state.update {
                        it.copy(
                            categoryItems = it.categoryItems.map {
                                it.copy(isClick = it.categoryId == event.categoryUi.categoryId)
                            },
                            recentlyCategoryItems = it.recentlyCategoryItems.map {
                                it.copy(isClick = false)
                            },
                        )
                    }

                }
                _state.update {
                    it.copy(
                        categoryUi = event.categoryUi,
                        description = event.description
                    )
                }

            }
            AddEvent.OnSaveClick -> {
                screenModelScope.launch {
                    val timestamp = DateConverter.localDateTimeToTimestamp(
                        localDateTime = state.value.nowLocalDateTime
                    )
                    if(state.value.currentExpense == null){
                        println("CategorUI ${state.value.categoryUi} / ${state.value.categoryUi?.toCategory()}")
                        val expense = Expense(
                            typeId = state.value.categoryUi?.typeId?:0,
                            categoryId = state.value.categoryUi?.categoryId?:0,
                            description = state.value.description,
                            isIncome = state.value.isIncome,
                            cost = state.value.cost.toLong(),
                            timestamp = timestamp
                        )
                        mogoDB.insertExpense(
                            expense
                        )
                        println("Insert ${expense}")
                    }else{
                        val updateExpense =  Expense(
                            description = state.value.description,
                            isIncome = state.value.isIncome,
                            typeId = state.value.categoryUi?.typeId?:0,
                            categoryId = state.value.categoryUi?.categoryId?:0,
                            cost = state.value.cost.toLong(),
                            id = state.value.currentExpense!!.id,
                            timestamp = timestamp
                        )
                        //update
                        mogoDB.updateExpense(
                            updateExpense
                        )
                        println("Update ${updateExpense}")
                    }
                    _uiEvent.send(
                        AddUiEvent.Success
                    )


                }
            }
        }
    }

}