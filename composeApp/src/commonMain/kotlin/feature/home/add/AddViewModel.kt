package feature.home.add

import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.data.MongoDB
import feature.core.domain.KeySettings
import feature.core.domain.mapper.toCategoryUi
import feature.core.domain.mapper.toTypeUi
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.domain.repository.ExpenseRepository
import feature.core.domain.repository.TypeRepository
import feature.core.presentation.CategoryList
import feature.core.presentation.date.DateConverter
import feature.core.presentation.model.CategoryUi
import feature.home.add.type.TypeUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.recently
import org.jetbrains.compose.resources.getString

class AddViewModel(
    private val repository: ExpenseRepository,
    private val typeRepository: TypeRepository,
): ViewModel() {

    companion object{
        private const val COST_MAX_LENGTH = 10
    }

    private val _state = MutableStateFlow(AddState())
    val state = _state.asStateFlow()

    private val _uiEvent = Channel<AddUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    init {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    isLoading = true
                )
            }
            val recentlyExpense = repository.getRecentlyExpenses()
            val types = typeRepository.getTypes()
            combine(recentlyExpense,types){ recentlyExpenseFlow , typeFlow ->
                val recentlyType = TypeUi(
                    typeIdTimestamp = -1,
                    name = getString(Res.string.recently),
                    colorArgb = -1,
                    isShow = true,
                    order = 0,
                    categories = recentlyExpenseFlow.mapIndexed { index, expense ->
                        CategoryUi(
                            id = expense.categoryId,
                            name = expense.description,
                            order = index,
                            typeId = expense.typeId.toLong(),
                            isClick = false,
                            colorArgb = CategoryList.getColorByTypeId(expense.typeId.toLong()).toArgb()
                        )
                    }
                )
                val typeFlowResult =  typeFlow.filter { it.isShow }.mapIndexed { _, type ->
                    type.toTypeUi().copy(
                        categories = type.categories.map {
                            it.toCategoryUi().copy(
                                colorArgb = type.colorArgb
                                )
                        }
                    )
                }
                _state.update {
                    it.copy(
                        types = typeFlowResult.sortedBy { it.order },
                        recentlyItems = listOf(recentlyType),
                        isLoading = false
                    )
                }


            }.launchIn(this)

        }

    }

    fun onEvent(event: AddEvent){
        when(event){
            is AddEvent.SetupExpense        -> {
//                if (event.expense != null){
//                    viewModelScope.launch {
//                        val expense = event.expense
//                        val localDateTime = DateConverter.getLocalDateTimeFromTimestamp(expense.timestamp)
//                        val recentlyExpenses = repository.getRecentlyExpenses()
//                        recentlyExpenses.collectLatest {
//                            val recentlyCategories = it.map { expense ->
////                                CategoryUi(
////                                    categoryId = expense.categoryId,
////                                    name = expense.description,
////                                    isClick = expense.idString == event.expense.idString,
////                                    typeId = expense.typeId.toLong()
////                                )
//                            }
//                            val allCategories = mutableListOf<CategoryUi>()
//                               state.value.expenseGroups.forEach {
//                                 it.categories.map { categoryUi ->
//                                       allCategories.add(
//                                           categoryUi
//                                       )
//                                   }
//                            }
//                            val stateCategories = allCategories
//
//                            val categories = if(recentlyCategories.any{ it.isClick }) {
//                                stateCategories.map {
//                                    it.copy(isClick = false, categoryId = it.categoryId , typeId =  it.typeId)
//                                }
//                            }else{
//                                stateCategories.map {
//                                    it.copy(
//                                        isClick = expense.categoryId == it.categoryId , categoryId = it.categoryId , typeId = it.typeId
//                                    )
//                                }
//                            }
//                            _state.update {
//                                it.copy(
//                                    currentExpense = expense,
//                                    year = localDateTime.year,
//                                    monthNumber = localDateTime.monthNumber,
//                                    dayOfMonth = localDateTime.dayOfMonth,
//                                    nowLocalDateTime = localDateTime,
//                                    isIncome = expense.isIncome,
//                                    cost = expense.cost.toString(),
//                                    categoryUi = CategoryUi(
//                                        typeId = expense.typeId.toLong(),
//                                        categoryId = expense.categoryId,
//                                        isClick = true
//                                    ),
//                                    description = expense.description,
//                                    recentlyCategoryItems = recentlyCategories
//                                )
//                            }
//                        }
//
//
//
//                    }
//
//                }else{
//                    viewModelScope.launch {
//                        val recentlyExpenses = repository.getRecentlyExpenses()
//                        recentlyExpenses.collectLatest {
//                            val recentlyCategories = it.mapIndexed  { index ,expense ->
//                                CategoryUi(
//                                    categoryId = expense.categoryId,
//                                    name = expense.description,
//                                    isClick = false,
//                                    typeId = expense.typeId.toLong(),
//                                    order = index
//                                )
//                            }
//                            val localDateTime = DateConverter.getNowDate()
//                            _state.update {
//                                it.copy(
//                                    year = localDateTime.year,
//                                    monthNumber = localDateTime.monthNumber,
//                                    dayOfMonth = localDateTime.dayOfMonth,
//                                    nowLocalDateTime = localDateTime,
//                                    recentlyCategoryItems = recentlyCategories,
//                                )
//                            }
//                        }
//
//                    }
//
//
//
//                }



            }
            is AddEvent.OnDescriptionChange -> {
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
            is AddEvent.OnCostChange        -> {
                val currentCost = state.value.cost
                if (currentCost.length > COST_MAX_LENGTH) return
                _state.update {
                    it.copy(
                        cost = currentCost + event.text
                    )
                }
            }
            AddEvent.OnDeleteTextClick      -> {
                val currentCost = state.value.cost
                if (currentCost.trim().isEmpty()) return
                _state.update {
                    it.copy(
                        cost = currentCost.dropLast(1)
                    )
                }
            }
            is AddEvent.OnTypeChange        -> {
                _state.update {
                    it.copy(
                        isIncome = event.isClicked
                    )
                }
            }
            is AddEvent.OnSelectedDate      -> {
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
            is AddEvent.OnItemSelected      -> {
//                if(event.isRecently){
//                    _state.update {
//                        it.copy(
//                            categoryItems = it.categoryItems.map {
//                                it.copy(isClick = false)
//                            },
//                            recentlyCategoryItems = it.recentlyCategoryItems.map {
//                                it.copy(isClick = it.id == event.categoryUi?.id)
//                            }
//                        )
//                    }
//                }else{
//                    _state.update {
//                        it.copy(
//                            categoryItems = it.categoryItems.map {
//                                it.copy(isClick = it.id == event.categoryUi?.id)
//                            },
//                            recentlyCategoryItems = it.recentlyCategoryItems.map {
//                                it.copy(isClick = false)
//                            },
//                        )
//                    }
//
//                }
//                _state.update {
//                    it.copy(
//                        categoryUi = event.categoryUi,
//                        description = event.description
//                    )
//                }

            }
            AddEvent.OnSaveClick            -> {
                viewModelScope.launch {
                    //TODO: Need Fix This feature
//                    val timestamp = DateConverter.localDateTimeToTimestamp(
//                        localDateTime = state.value.nowLocalDateTime
//                    )
//                    if(state.value.currentExpense == null){
//                        val expense = Expense(
//                            typeId = state.value.categoryUi?.typeId?:0,
//                            categoryId = state.value.categoryUi?.categoryId?:0,
//                            description = state.value.description,
//                            isIncome = state.value.isIncome,
//                            cost = state.value.cost.toLongOrNull()?:0L,
//                            timestamp = timestamp,
//                        )
//                        repository.insertExpense(
//                            expense
//                        )
//                    }else{
//                        val updateExpense =  Expense(
//                            description = state.value.description,
//                            isIncome = state.value.isIncome,
//                            typeId = state.value.categoryUi?.typeId?:0,
//                            categoryId = state.value.categoryUi?.categoryId?:0,
//                            cost = state.value.cost.toLongOrNull()?:0L,
//                            id = state.value.currentExpense!!.id,
//                            timestamp = timestamp
//                        )
//                        repository.updateExpense(
//                            updateExpense
//                        )
//                    }
//                    _uiEvent.send(
//                        AddUiEvent.Success
//                    )


                }
            }

            else                            -> {}
        }
    }
}