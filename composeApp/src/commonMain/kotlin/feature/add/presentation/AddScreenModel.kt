package feature.add.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.core.data.MongoDB
import feature.core.domain.mapper.toCategory
import feature.core.domain.model.Expense
import feature.core.presentation.CategoryList
import feature.core.presentation.date.DateConverter
import feature.core.presentation.model.CategoryUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AddScreenModel(
    private val mongoDB: MongoDB
): ScreenModel {

    companion object{
        private const val COST_MAX_LENGTH = 10
    }

    private val _state = MutableStateFlow(AddState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<AddUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    fun onEvent(event: AddEvent){
        when(event){
            is AddEvent.SetupExpense -> {
                if (event.expense != null){
                    screenModelScope.launch {
                        val expense = event.expense
                        val localDateTime = DateConverter.getLocalDateTimeFromTimestamp(expense.timestamp)
                        val recentlyExpenses = mongoDB.getExpenseLastEightItems()
                        recentlyExpenses.collectLatest {
                            val recentlyCategories = it.map { expense ->
                                CategoryUi(
                                    id = CategoryList.RECENTLY + expense.id.hashCode(),
                                    categoryId = expense.categoryId,
                                    name = expense.description,
                                    isClick = expense.id.hashCode() == event.expense.id.hashCode(),
                                    typeId = expense.typeId
                                )
                            }
                            val stateCategories = state.value.categoryItems
                            val categories = if(recentlyCategories.any{ it.isClick }) {
                                stateCategories.map {
                                    it.copy(isClick = false, categoryId = it.categoryId , typeId =  it.typeId)
                                }
                            }else{
                                stateCategories.map {
                                    it.copy(
                                        isClick = expense.categoryId == it.categoryId , categoryId = it.categoryId , typeId = it.typeId
                                    )
                                }
                            }


                            _state.update {
                                it.copy(
                                    currentExpense = expense,
                                    year = localDateTime.year,
                                    monthNumber = localDateTime.monthNumber,
                                    dayOfMonth = localDateTime.dayOfMonth,
                                    nowLocalDateTime = localDateTime,
                                    isIncome = expense.isIncome,
                                    cost = expense.cost.toString(),
                                    categoryUi = CategoryUi(
                                        typeId = expense.typeId,
                                        categoryId = expense.categoryId,
                                        isClick = true
                                    ),
                                    description = expense.description,
                                    recentlyCategoryItems = recentlyCategories,
                                    categoryItems = categories
                                )
                            }

                        }

                    }

                }else{
                    screenModelScope.launch {
                        val recentlyExpenses = mongoDB.getExpenseLastEightItems()
                        recentlyExpenses.collectLatest {
                            val recentlyCategories = it.map { expense ->
                                CategoryUi(
                                    id = CategoryList.RECENTLY + expense.id.hashCode(),
                                    categoryId = expense.categoryId,
                                    name = expense.description,
                                    isClick = false,
                                    typeId = expense.typeId
                                )
                            }
                            val localDateTime = DateConverter.getNowDate()
                            _state.update {
                                it.copy(
                                    year = localDateTime.year,
                                    monthNumber = localDateTime.monthNumber,
                                    dayOfMonth = localDateTime.dayOfMonth,
                                    nowLocalDateTime = localDateTime,
                                    recentlyCategoryItems = recentlyCategories,
                                )
                            }
                        }

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
                        dayOfMonth = localDateTime.dayOfMonth,
                        nowLocalDateTime = localDateTime
                    )
                }
            }
            is AddEvent.OnItemSelected -> {
                if(event.isRecently){
                    _state.update {
                        it.copy(
                            categoryItems = it.categoryItems.map {
                                it.copy(isClick = false)
                            },
                            recentlyCategoryItems = it.recentlyCategoryItems.map {
                                it.copy(isClick = it.id == event.categoryUi.id)
                            }
                        )
                    }
                }else{
                    _state.update {
                        it.copy(
                            categoryItems = it.categoryItems.map {
                                it.copy(isClick = it.id == event.categoryUi.id)
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
                        val expense = Expense(
                            typeId = state.value.categoryUi?.typeId?:0,
                            categoryId = state.value.categoryUi?.categoryId?:0,
                            description = state.value.description,
                            isIncome = state.value.isIncome,
                            cost = state.value.cost.toLongOrNull()?:0L,
                            timestamp = timestamp,
                        )
                        mongoDB.insertExpense(
                            expense
                        )
                    }else{
                        val updateExpense =  Expense(
                            description = state.value.description,
                            isIncome = state.value.isIncome,
                            typeId = state.value.categoryUi?.typeId?:0,
                            categoryId = state.value.categoryUi?.categoryId?:0,
                            cost = state.value.cost.toLongOrNull()?:0L,
                            id = state.value.currentExpense!!.id,
                            timestamp = timestamp
                        )
                        mongoDB.updateExpense(
                            updateExpense
                        )
                    }
                    _uiEvent.send(
                        AddUiEvent.Success
                    )


                }
            }
        }
    }

}