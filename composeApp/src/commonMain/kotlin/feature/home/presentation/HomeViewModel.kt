package feature.home.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.model.SpendingLimit
import core.domain.repository.ExpenseRepository
import core.domain.repository.SpendingLimitRepository
import core.domain.repository.TypeRepository
import core.presentation.date.DateConverter
import core.presentation.date.DateConverter.getNowDate
import core.presentation.date.toDayString
import feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository,
    private val typeRepository: TypeRepository,
    private val spendingLimitRepository: SpendingLimitRepository
): ViewModel() {

    private var currentSpendingLimit: SpendingLimit? = null

    private val _state = MutableStateFlow(HomeState())
    val state = _state.onStart {
        viewModelScope.launch {
            typeRepository.getTypes().collectLatest { data ->
                _state.update {
                    it.copy(
                        typesItem = data
                    )
                }
                onEvent(HomeEvent.OnDatePick(isInit = true))
            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.OnDatePick -> {
                viewModelScope.launch {
                    val nowDate = getNowDate()
                    val nowYear = nowDate.year
                    val nowMonth = nowDate.monthNumber

                    val (startTime,endTime) = DateConverter.getMonthStartAndEndTime(
                        year =  event.year,
                        month = event.month
                    )
                    launch {
                        val spendingLimitResult = spendingLimitRepository.getSpendingLimit(
                            year = event.year?:nowYear,
                            month = event.month?:nowMonth
                        )
                        spendingLimitResult.collectLatest { spendingLimit ->
                            currentSpendingLimit = spendingLimit
                            _state.update { state ->
                                state.copy(
                                    expenseLimit = currentSpendingLimit?.limit?:0
                                )
                            }
                        }
                    }

                    launch {
                        val result = repository.getExpenseByTime(
                            startTimeOfMonth = startTime,
                            endTimeOfMonth = endTime
                        )
                        result.collectLatest { data ->
                            val expenseItems = data.filterNot { it.isIncome }
                            val totalExpense = expenseItems.sumOf { it.cost }
                            val dataGroup = expenseItems
                                .sortedByDescending {
                                    it.timestamp
                                }
                                .groupBy {
                                    it.timestamp.toDayString()
                                }
                                .toList()
                            if (event.isInit){
                                _state.update {
                                    it.copy(
                                        nowDateDayOfMonth = nowDate.dayOfMonth.toString()
                                    )
                                }
                            }
                            _state.update {
                                it.copy(
                                    totalExpense = totalExpense,
                                    items = dataGroup,
                                    nowDateYear = if (event.year == null) {
                                        nowYear.toString()
                                    }else {
                                        event.year.toString()
                                    },
                                    nowDateMonth = if (event.month == null) {
                                        nowMonth.toString()
                                    }else {
                                        event.month.toString()
                                    },
                                )
                            }
                        }

                    }
                }
            }
            is HomeEvent.OnSpendingLimitChange ->{
                val expenseLimit = event.expenseLimitText.toLongOrNull()
                expenseLimit?.let { limit ->
                    viewModelScope.launch {
                        if (currentSpendingLimit != null){
                            val spendingLimit = currentSpendingLimit!!
                            spendingLimitRepository.update(
                                spendingLimit = spendingLimit.copy(
                                    limit =  limit
                                )
                            )
                        }else{
                            val nowDate = getNowDate()
                            val nowYear = nowDate.year
                            val nowMonth = nowDate.monthNumber
                            spendingLimitRepository.insert(
                                SpendingLimit(
                                    limit =  limit,
                                    year = nowYear,
                                    month = nowMonth
                                )
                            )
                        }

                    }
                    _state.update {
                        it.copy(
                            expenseLimit = limit
                        )
                    }
                }
            }

            else -> Unit
        }
    }

}