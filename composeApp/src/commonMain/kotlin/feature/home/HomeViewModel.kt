package feature.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.data.MongoDB
import feature.core.presentation.date.DateConverter
import feature.core.presentation.date.DateConverter.getNowDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val mongoDB: MongoDB
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
        onEvent(HomeEvent.OnDatePick(isInit = true))
    }

    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.OnDatePick -> {
                viewModelScope.launch {
                    val (startTime,endTime) = DateConverter.getMonthStartAndEndTime(
                        year =  event.year,
                        month = event.month
                    )
                    val result = mongoDB.getExpenseByStartTimeAndEndTime(
                        startTimeOfMonth = startTime,
                        endTimeOfMonth = endTime
                    )
                    result.collectLatest { data ->
                        val dataGroup = data
                            .sortedByDescending {
                                it.timestamp
                            }
                            .groupBy {
                                DateConverter.getDayStringDefault(it.timestamp)
                            }
                            .toList()
                        val incomeItems = data.filter { it.isIncome }
                        val expenseItems = data.filterNot { it.isIncome }
                        val income = incomeItems.sumOf { it.cost }
                        val expense = expenseItems.sumOf { it.cost }
                        val total = -expense + income
                        if (event.isInit){
                            _state.update {
                                it.copy(
                                    nowDateDayOfMonth = getNowDate().dayOfMonth.toString()
                                )
                            }
                        }
                        _state.update {
                            it.copy(
                                income = income,
                                expense = expense,
                                totalAmount = total,
                                items = dataGroup,
                                nowDateYear = if (event.year == null) {
                                    getNowDate().year.toString()
                                }else {
                                    event.year.toString()
                                },
                                nowDateMonth = if (event.month == null) {
                                    getNowDate().monthNumber.toString()
                                }else {
                                    event.month.toString()
                                },
                            )
                        }
                    }
                }
            }
        }
    }

}