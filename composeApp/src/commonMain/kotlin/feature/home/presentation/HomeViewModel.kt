package feature.home.presentation

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.core.domain.repository.ExpenseRepository
import feature.core.domain.repository.TypeRepository
import feature.core.presentation.date.DateConverter
import feature.core.presentation.date.DateConverter.getNowDate
import feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository,
    private val typeRepository: TypeRepository
): ViewModel() {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    init {
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
    }

    fun onEvent(event: HomeEvent){
        when(event){
            is HomeEvent.OnDatePick -> {
                viewModelScope.launch {
                    val (startTime,endTime) = DateConverter.getMonthStartAndEndTime(
                        year =  event.year,
                        month = event.month
                    )
                    val result = repository.getExpenseByTime(
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
            else -> Unit
        }
    }

}