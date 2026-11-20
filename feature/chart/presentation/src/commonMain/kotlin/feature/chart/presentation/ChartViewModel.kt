package feature.chart.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.repository.SpendingLimitRepository
import core.presentation.date.DateConverter
import core.presentation.date.DateConverter.getNowDate
import core.presentation.model.MonthYear
import feature.chart.domain.repository.ChartRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn

@OptIn(ExperimentalCoroutinesApi::class)
class ChartViewModel(
    private val repository: ChartRepository,
    private val spendingLimitRepository: SpendingLimitRepository
) : ViewModel() {

    private val selectedMonthYear = MutableStateFlow(MonthYear())

    val state: StateFlow<ChartState> = selectedMonthYear
        .flatMapLatest { (year, month) ->
            val nowDate = getNowDate()
            val targetYear = year ?: nowDate.year
            val targetMonth = month ?: nowDate.monthNumber
            val (startTime, endTime) = DateConverter.getMonthStartAndEndTime(
                year = year,
                month = month
            )

            combine(
                spendingLimitRepository.getSpendingLimit(
                    year = targetYear,
                    month = targetMonth
                ),
                repository.getExpenseByTime(
                    startTimeOfMonth = startTime,
                    endTimeOfMonth = endTime
                )
            ) { spendingLimit, resultData ->
                val sortedItems = resultData.sortedByDescending { chart ->
                    chart.items.sumOf { it.cost }
                }
                val totalExpense = sortedItems.sumOf { chart ->
                    chart.items.sumOf { it.cost }
                }
                ChartState(
                    items = sortedItems,
                    totalExpense = totalExpense,
                    spendingLimit = spendingLimit?.limit ?: 0L,
                    nowDateYear = targetYear.toString(),
                    nowDateMonth = targetMonth.toString()
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = ChartState()
        )

    fun onAction(event: ChartEvent) {
        when (event) {
            is ChartEvent.OnDatePick -> {
                selectedMonthYear.value = MonthYear(year = event.year, month = event.month)
            }
            else -> Unit
        }
    }
}
