package feature.chart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.chart.domain.repository.ChartRepository
import feature.core.domain.model.Expense
import feature.core.domain.model.chart.Chart
import feature.core.domain.repository.ExpenseRepository
import feature.core.presentation.date.DateConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChartViewModel(
    private val repository: ChartRepository
): ViewModel() {

    private val _state = MutableStateFlow(ChartState())
    val state = _state.asStateFlow()

    private val _dbExpenseState = MutableStateFlow(listOf<Expense>())

    fun onEvent(event: ChartEvent){
        when(event){
            is ChartEvent.OnTypeChange -> {
                if (state.value.isIncomeShown == event.isIncome) return
                _state.update {
                    it.copy(
                        isIncomeShown = event.isIncome
                    )
                }
            }
            is ChartEvent.OnDatePick   -> {
                viewModelScope.launch {
                    val (startTime,endTime) = DateConverter.getMonthStartAndEndTime(
                        year =  event.year,
                        month = event.month
                    )
                    val result = repository.getExpenseByTime(
                        startTimeOfMonth = startTime,
                        endTimeOfMonth = endTime
                    )
                    result.collectLatest { resultData ->
                        val nowDateYear = event.year ?: DateConverter.getNowDate().year
                        val nowDateMonth = event.month ?: DateConverter.getNowDate().monthNumber
                        val sortedItems = resultData.sortedByDescending {
                            if (state.value.isIncomeShown){
                                it.itemsIncome.sumOf { it.cost }
                            }else{
                                it.itemsNotIncome.sumOf { it.cost }
                            }
                        }
                        _state.update {
                            it.copy(
                                items = sortedItems,
                                nowDateYear = nowDateYear.toString(),
                                nowDateMonth = nowDateMonth.toString()
                            )
                        }
                    }
                }
            }
        }
    }
}