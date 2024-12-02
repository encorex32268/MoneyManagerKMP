package feature.home.presentation.add

import AdMobBannerController
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.toArgb
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.data.DefaultKeySettings
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
import feature.core.presentation.date.toLocalDateTime
import feature.core.presentation.date.toTimestamp
import feature.core.presentation.model.CategoryUi
import feature.home.presentation.add.type.TypeUi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.serialization.Serializable
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.recently
import org.jetbrains.compose.resources.getString
import kotlin.math.exp

class AddViewModel(
    private val repository: ExpenseRepository,
    private val typeRepository: TypeRepository,
    private val expense: Expense?=null,
    private val keySettings: DefaultKeySettings
): ViewModel() {

    companion object{
        private const val COST_MAX_LENGTH = 10
        private const val CLOSE_AD = "moneymanagerclose"
        private const val OPEN_AD = "moneymanageropen"
    }

    private val _uiEvent = Channel<AddUiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    private val _state = MutableStateFlow(AddState(currentExpense = expense))
    val state = _state.onStart {
        viewModelScope.launch {
            val localDateTime = expense?.timestamp?.toLocalDateTime() ?: DateConverter.getNowDate()
            val recentlyExpense = repository.getRecentlyExpenses()
            val types = typeRepository.getTypes()
            combine(recentlyExpense,types){ recentlyExpenseFlow , typeFlow ->
                var isIconClicked = false
                val recentlyType = TypeUi(
                    typeIdTimestamp = -1,
                    name = getString(Res.string.recently),
                    colorArgb = -1,
                    isShow = true,
                    order = 0,
                    categories = recentlyExpenseFlow.mapIndexed { index, recentlyExpense ->
                        val findType = typeFlow.find { type ->
                            type.typeIdTimestamp == recentlyExpense.typeId
                        }
                        val colorArgb = findType?.colorArgb ?:CategoryList.getColorByTypeId(recentlyExpense.typeId).toArgb()

                        val isSameIconFound = recentlyExpense.idString == expense?.idString
                        if (isSameIconFound){
                            isIconClicked = true
                        }
                        CategoryUi(
                            id = recentlyExpense.categoryId,
                            name = recentlyExpense.description,
                            order = index,
                            typeId = recentlyExpense.typeId,
                            isClick = recentlyExpense.idString == expense?.idString,
                            colorArgb = colorArgb
                        )
                    }
                )
                val typeFlowResult =  typeFlow.filter { it.isShow }.mapIndexed { _, type ->
                    type.toTypeUi().copy(
                        categories = type.categories.map {
                            it.toCategoryUi().copy(
                                typeId = type.typeIdTimestamp,
                                colorArgb = type.colorArgb,
                                isClick = if (isIconClicked) false else expense?.typeId == it.typeId && expense?.categoryId == it.id
                            )

                        }
                    )
                }
                _state.update {
                    it.copy(
                        types = typeFlowResult.sortedBy { it.order },
                        recentlyItems = recentlyType,
                        year = localDateTime.year,
                        monthNumber = localDateTime.monthNumber,
                        dayOfMonth = localDateTime.dayOfMonth,
                        nowLocalDateTime = localDateTime,
                        currentExpense = expense,
                        isIncome = expense?.isIncome?:false,
                        cost = expense?.let { expense.cost.toString() }?:"0",
                        categoryUi = expense?.let {
                            val findType = typeFlow.find { type ->
                                type.typeIdTimestamp == it.typeId
                            }
                            val colorArgb = findType?.colorArgb ?:CategoryList.getColorByTypeId(it.typeId).toArgb()
                            CategoryUi(
                                typeId = expense.typeId,
                                id = expense.categoryId,
                                isClick = true,
                                order = 0,
                                name = expense.description,
                                colorArgb = colorArgb
                            )
                        },
                        description = expense?.description?:""
                    )
                }
            }
        }
    }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )


    fun onEvent(event: AddEvent){
        when(event){
            is AddEvent.OnDescriptionChange -> {
                when(event.description){
                    CLOSE_AD -> {
                        keySettings.setCloseAdBanner(true)
                        AdMobBannerController.setCloseAdMobBanner(true)
                    }
                    OPEN_AD -> {
                        keySettings.setCloseAdBanner(false)
                        AdMobBannerController.setCloseAdMobBanner(false)
                    }
                }
                _state.update {
                    it.copy(
                        description = event.description
                    )
                }
            }
            is AddEvent.OnCostChange        -> {
                val currentCost = state.value.cost
                if (currentCost.length > COST_MAX_LENGTH)  return
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
                val localDateTime = event.timestamp.toLocalDateTime()
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
                if(event.isRecently){
                    _state.update {
                        it.copy(
                            recentlyItems = state.value.recentlyItems?.copy(
                                categories = state.value.recentlyItems!!.categories.map {
                                    if (event.categoryUi?.typeId == it.typeId && event.categoryUi?.order == it.order){
                                        it.copy(isClick = true)
                                    }else{
                                        it.copy(isClick = false)
                                    }
                                }
                            ),
                            types = state.value.types.map {
                                it.copy(
                                    categories = it.categories.map { category ->
                                        category.copy(isClick = false)
                                    }
                                )
                            },
                            categoryUi = event.categoryUi
                        )
                    }
                }else{
                    _state.update {
                        it.copy(
                            recentlyItems = state.value.recentlyItems?.copy(
                                categories = state.value.recentlyItems!!.categories.map {
                                    it.copy(isClick = false)
                                }
                            ),
                            types = state.value.types.map {
                                it.copy(
                                    categories = it.categories.map { category ->
                                        if (event.categoryUi?.typeId == category.typeId && event.categoryUi?.order == category.order){
                                            category.copy(isClick = true)
                                        }else{
                                            category.copy(isClick = false)
                                        }
                                    }
                                )
                            },
                            categoryUi = event.categoryUi
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
            AddEvent.OnSaveClick            -> {
                viewModelScope.launch {
                    val timestamp = state.value.nowLocalDateTime.toTimestamp()
                    if(state.value.currentExpense == null){
                        val expense = Expense(
                            typeId = state.value.categoryUi?.typeId?:0,
                            categoryId = state.value.categoryUi?.id?:0,
                            description = state.value.description,
                            isIncome = state.value.isIncome,
                            cost = state.value.cost.toLongOrNull()?:0L,
                            timestamp = timestamp
                        )
                        repository.insert(
                            expense
                        )
                    }else{
                        val updateExpense =  Expense(
                            description = state.value.description,
                            isIncome = state.value.isIncome,
                            typeId = state.value.categoryUi?.typeId?:0,
                            categoryId = state.value.categoryUi?.id?:0,
                            cost = state.value.cost.toLongOrNull()?:0L,
                            id = state.value.currentExpense!!.id,
                            timestamp = timestamp,
                            content = state.value.currentExpense?.content?:""
                        )
                        repository.update(
                            updateExpense
                        )
                    }
                    _uiEvent.send(
                        AddUiEvent.Success
                    )


                }
            }
            else -> Unit
        }
    }
}