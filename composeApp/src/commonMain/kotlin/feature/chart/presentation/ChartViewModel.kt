package feature.chart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.chart.domain.repository.ChartRepository
import core.domain.repository.SpendingLimitRepository
import core.presentation.date.DateConverter
import core.presentation.date.DateConverter.getNowDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChartViewModel(
    private val repository: ChartRepository,
    private val spendingLimitRepository: SpendingLimitRepository
): ViewModel() {

    private val _state = MutableStateFlow(ChartState())
    val state =
        _state
            .onStart {
                onEvent(ChartEvent.OnDatePick())
            }
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5000L),
                _state.value
            )


    fun onEvent(event: ChartEvent){
        when(event){
            is ChartEvent.OnDatePick   -> {
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
                            _state.update { state ->
                                state.copy(
                                    spendingLimit = spendingLimit?.limit?:0L
                                )
                            }
                        }
                    }

                    launch {
                        repository.getExpenseByTime(
                            startTimeOfMonth = startTime,
                            endTimeOfMonth = endTime
                        ).collectLatest { resultData ->
                            val nowDateYear = event.year ?: nowYear
                            val nowDateMonth = event.month ?: nowMonth

                            val sortedItems = resultData.sortedByDescending {
                                it.items.sumOf { it.cost }
                            }
                            var totalExpense = 0L
                            sortedItems.forEach { chart ->
                                chart.items.forEach {
                                    totalExpense += it.cost
                                }
                            }

                            _state.update {
                                it.copy(
                                    items = sortedItems,
                                    nowDateYear = nowDateYear.toString(),
                                    nowDateMonth = nowDateMonth.toString(),
                                    totalExpense = totalExpense
                                )
                            }
                        }
                    }

                }
            }
            else -> Unit
        }
    }
}