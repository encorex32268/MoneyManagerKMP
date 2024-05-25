package feature.chart.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.chart.domain.model.Chart
import feature.core.data.MongoDB
import feature.core.domain.model.Expense
import feature.core.presentation.date.DateConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ChartScreenModel(
    private val mongoDB: MongoDB
): ScreenModel {

    private val _state = MutableStateFlow(ChartState())
    val state = _state.asStateFlow()

    private val _dbExpenseState = MutableStateFlow(listOf<Expense>())
    private val dbExpenseState = _dbExpenseState.asStateFlow()

    fun onEvent(event: ChartEvent){
        when(event){
            ChartEvent.OnChartClick -> {
                val expensesTypeList  = dbExpenseState.value
                    .filter {
                        it.isIncome != state.value.isIncomeShow
                    }.groupBy {
                        it.typeId
                }.toList().sortedBy {
                    it.second.sumOf { it.cost }
                }
                _state.update {
                    it.copy(
                        isIncomeShow = !state.value.isIncomeShow,
                        expensesTypeList = expensesTypeList
                    )
                }
            }
            is ChartEvent.OnDatePick -> {
                screenModelScope.launch {
                    val (startTime,endTime) = DateConverter.getMonthStartAndEndTime(
                        year =  event.year,
                        month = event.month
                    )
                    val result = mongoDB.getExpenseByStartTimeAndEndTime(
                        startTimeOfMonth = startTime,
                        endTimeOfMonth = endTime
                    )
                    result.collectLatest { resultData ->
                        _dbExpenseState.update { resultData }
                        val dataGroup = resultData.groupBy {
                            it.typeId
                        }
                        val expensesTypeList =
                            resultData.filter { it.isIncome == state.value.isIncomeShow }
                                .groupBy { it.typeId ?: 0 }
                                .toList()
                                .sortedByDescending {
                                    it.second.sumOf {
                                        it.cost
                                    }
                                }
                        val itemCharts = dataGroup.map {
                            val typeId = it.key
                            val items = it.value
                            Chart(
                                typeId = typeId,
                                income = items.filter { it.isIncome }.sumOf { it.cost },
                                expense = items.filterNot { it.isIncome }.sumOf { it.cost },
                                detailItems = items
                            )
                        }
                        val nowDateYear = event.year ?: DateConverter.getNowDate().year
                        val nowDateMonth = event.month ?: DateConverter.getNowDate().monthNumber
                        _state.update {
                            it.copy(
                                items = itemCharts,
                                expensesTypeList = expensesTypeList,
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