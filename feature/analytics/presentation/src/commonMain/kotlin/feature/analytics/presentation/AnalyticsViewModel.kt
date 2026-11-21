package feature.analytics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.model.Expense
import core.presentation.date.DateConverter
import core.presentation.date.DateConverter.getNowDate
import core.presentation.date.toEpochMilliseconds
import core.presentation.date.toStringDateByTimestamp
import core.presentation.date.toStringDateMByTimestamp
import core.presentation.date.toStringDateMDByTimestamp
import feature.analytics.domain.AnalyticsRepository
import feature.analytics.presentation.model.DataPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class AnalyticsViewModel(
    private val repository: AnalyticsRepository
) : ViewModel() {

    private val filterState = MutableStateFlow(FilterState())

    val state: StateFlow<AnalyticsState> = combine(
        repository.getAllExpense(),
        filterState
    ) { allExpenses, filter ->
        val expenses = allExpenses.filter { !it.isIncome }
        val nowDate = getNowDate()
        val displayYear = if (filter.dateFilter == DateFilter.ONE_MONTH) {
            filter.selectedYear ?: nowDate.year
        } else {
            nowDate.year
        }
        val displayMonth = if (filter.dateFilter == DateFilter.ONE_MONTH) {
            filter.selectedMonth ?: nowDate.monthNumber
        } else {
            nowDate.monthNumber
        }
        val dataPoints = filterExpenseByDate(
            dateFilter = filter.dateFilter,
            items = expenses,
            selectedYear = filter.selectedYear,
            selectedMonth = filter.selectedMonth
        )
        AnalyticsState(
            nowDateYear = displayYear.toString(),
            nowDateMonth = displayMonth.toString(),
            dateFilter = filter.dateFilter,
            dataPoints = dataPoints,
            selectedDataPoint = filter.selectedDataPoint,
            expenseSum = dataPoints.sumOf { it.y.toLong() }
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000L),
        initialValue = AnalyticsState()
    )

    fun onAction(event: AnalyticsAction) {
        when (event) {
            is AnalyticsAction.OnDateFilterChange -> onDateFilterChange(event.dateFilter)
            is AnalyticsAction.OnSelectDataPoint -> onSelectDataPoint(event.dataPoint)
            is AnalyticsAction.OnDatePick -> onDatePick(event.year, event.month)
            else -> Unit
        }
    }

    private fun onDateFilterChange(dateFilter: DateFilter) {
        filterState.update {
            it.copy(
                dateFilter = dateFilter,
                selectedYear = null,
                selectedMonth = null,
                selectedDataPoint = null
            )
        }
    }

    private fun onSelectDataPoint(dataPoint: DataPoint?) {
        filterState.update {
            it.copy(selectedDataPoint = dataPoint)
        }
    }

    private fun onDatePick(year: Int?, month: Int?) {
        if (filterState.value.dateFilter != DateFilter.ONE_MONTH) return
        filterState.update {
            it.copy(
                selectedYear = year,
                selectedMonth = month,
                selectedDataPoint = null
            )
        }
    }

    private fun filterExpenseByDate(
        dateFilter: DateFilter,
        items: List<Expense>,
        selectedYear: Int? = null,
        selectedMonth: Int? = null
    ): List<DataPoint> {
        return when (dateFilter) {
            DateFilter.ALL -> {
                items
                    .sortedBy { it.timestamp }
                    .groupBy { it.timestamp.toStringDateMByTimestamp() }
                    .map {
                        DataPoint(
                            x = it.key.length.toFloat(),
                            y = it.value.sumOf { expense -> expense.cost }.toFloat(),
                            xLabel = it.key
                        )
                    }
            }
            DateFilter.ONE_MONTH -> {
                val nowDate = getNowDate()
                val (startTime, endTime) = DateConverter.getMonthStartAndEndTime(
                    year = selectedYear ?: nowDate.year,
                    month = selectedMonth ?: nowDate.monthNumber
                )
                items
                    .filter { it.timestamp in LongRange(startTime, endTime) }
                    .sortedBy { it.timestamp }
                    .groupBy { it.timestamp.toStringDateMDByTimestamp() }
                    .map {
                        DataPoint(
                            x = it.key.length.toFloat(),
                            y = it.value.sumOf { expense -> expense.cost }.toFloat(),
                            xLabel = it.key
                        )
                    }
            }
            else -> {
                items
                    .filter { it.timestamp in getTimestampRangeByDateFilter(dateFilter) }
                    .sortedBy { it.timestamp }
                    .groupBy {
                        when (dateFilter) {
                            DateFilter.SEVEN_DAYS -> it.timestamp.toStringDateMDByTimestamp()
                            DateFilter.ONE_MONTH -> it.timestamp.toStringDateMDByTimestamp()
                            DateFilter.THREE_MONTHS,
                            DateFilter.SIX_MONTHS,
                            DateFilter.ONE_YEAR -> it.timestamp.toStringDateMByTimestamp()
                            else -> it.timestamp.toStringDateByTimestamp()
                        }
                    }
                    .map {
                        DataPoint(
                            x = it.key.length.toFloat(),
                            y = it.value.sumOf { expense -> expense.cost }.toFloat(),
                            xLabel = it.key
                        )
                    }
            }
        }
    }

    private fun getTimestampRangeByDateFilter(
        dateFilter: DateFilter = DateFilter.SEVEN_DAYS
    ): LongRange {
        val now = getNowDate()
        val nowDate = LocalDate(
            year = now.year,
            monthNumber = now.monthNumber,
            dayOfMonth = now.dayOfMonth + 1
        )
        return when (dateFilter) {
            DateFilter.SEVEN_DAYS -> {
                val startDayOfMonth = nowDate.toEpochMilliseconds()
                val sevenDaysBefore = nowDate.minus(DatePeriod(days = 7)).toEpochMilliseconds()
                LongRange(sevenDaysBefore, startDayOfMonth)
            }
            DateFilter.ONE_MONTH -> {
                val pair = DateConverter.getMonthStartAndEndTime(
                    year = nowDate.year,
                    month = nowDate.monthNumber
                )
                LongRange(pair.first, pair.second)
            }
            DateFilter.THREE_MONTHS -> {
                val startDayOfMonth = nowDate.toEpochMilliseconds()
                val minus3MonthsLocalDate = nowDate.minus(DatePeriod(months = 3))
                val endDay = LocalDate(
                    year = minus3MonthsLocalDate.year,
                    monthNumber = minus3MonthsLocalDate.monthNumber,
                    dayOfMonth = 1
                ).toEpochMilliseconds()
                LongRange(endDay, startDayOfMonth)
            }
            DateFilter.SIX_MONTHS -> {
                val startDayOfMonth = nowDate.toEpochMilliseconds()
                val minus6MonthsLocalDate = nowDate.minus(DatePeriod(months = 6))
                val endDay = LocalDate(
                    year = minus6MonthsLocalDate.year,
                    monthNumber = minus6MonthsLocalDate.monthNumber,
                    dayOfMonth = 1
                ).toEpochMilliseconds()
                LongRange(endDay, startDayOfMonth)
            }
            DateFilter.ONE_YEAR -> {
                val startDayOfMonth = nowDate.toEpochMilliseconds()
                val endDate = LocalDate(
                    year = now.year,
                    monthNumber = 1,
                    dayOfMonth = 1
                ).toEpochMilliseconds()
                LongRange(endDate, startDayOfMonth)
            }
            DateFilter.ALL -> LongRange(1, 1)
        }
    }
}
