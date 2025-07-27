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
import feature.home.domain.mapper.toExpenseUi
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
    private val spendingLimitRepository: SpendingLimitRepository
): ViewModel() {

    private var hasInitialLoadedData = false
    private var currentSpendingLimit: SpendingLimit? = null

    private val _state = MutableStateFlow(HomeState())
    val state = _state.onStart {
        if (!hasInitialLoadedData){
            initDatePicker()
            hasInitialLoadedData = true
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        HomeState()
    )

    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.OnDatePick -> onDatePick(event.month , event.year)
            is HomeEvent.OnSpendingLimitChange -> onSpendingLimitChange(event.expenseLimitText)
            HomeEvent.OnExpenseLimitClick -> Unit
            HomeEvent.OnGotoAddScreen -> Unit
            is HomeEvent.OnGotoEditScreen -> Unit
        }
    }

    private fun initDatePicker(){
        onDatePick(null,null)
    }


    private fun onSpendingLimitChange(expenseLimitText: String) {
        val expenseLimit = expenseLimitText.toLongOrNull()
        expenseLimit?.let { limit ->
            viewModelScope.launch {
                if (currentSpendingLimit != null){
                    val spendingLimit = currentSpendingLimit
                    spendingLimit?.let {
                        spendingLimitRepository.update(
                            spendingLimit = spendingLimit.copy(
                                limit =  limit
                            )
                        )
                    }
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

    private fun onDatePick(month: Int?, year: Int?) {

        viewModelScope.launch {
            val nowDate = getNowDate()
            val nowYear = nowDate.year
            val nowMonth = nowDate.monthNumber

            val (startTime,endTime) = DateConverter.getMonthStartAndEndTime(
                year =  year,
                month = month
            )
            launch {
                val spendingLimitResult = spendingLimitRepository.getSpendingLimit(
                    year = year?:nowYear,
                    month = month?:nowMonth
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
                        .map { it.toExpenseUi() }
                        .sortedByDescending {
                            it.timestamp
                        }
                        .groupBy {
                            it.timestamp.toDayString()
                        }
                        .toList()
                    AppLogger.d("HomeViewModel","$dataGroup")
                    _state.update {
                        it.copy(
                            totalExpense = totalExpense,
                            items = dataGroup,
                            nowDateYear = year?.toString() ?: nowYear.toString(),
                            nowDateMonth = month?.toString() ?: nowMonth.toString(),
                            nowDateDayOfMonth = nowDate.dayOfMonth.toString()
                        )
                    }
                }

            }
        }
    }

}