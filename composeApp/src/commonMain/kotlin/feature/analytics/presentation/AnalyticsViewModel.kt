package feature.analytics.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import feature.analytics.domain.AnalyticsRepository
import feature.analytics.presentation.model.DataPoint
import feature.core.domain.model.Expense
import feature.core.presentation.date.DateConverter
import feature.core.presentation.date.toDayString
import feature.core.presentation.date.toEpochMilliseconds
import feature.core.presentation.date.toStringDateByTimestamp
import feature.core.presentation.date.toStringDateMByTimestamp
import feature.core.presentation.date.toStringDateMDByTimestamp
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus

class AnalyticsViewModel(
    private val repository: AnalyticsRepository
): ViewModel() {

    private val currentExpenseList = MutableStateFlow(emptyList<Expense>())

    private val _state = MutableStateFlow(AnalyticsState())
    val state = _state.onStart {
        viewModelScope.launch {
            repository.getAllExpense().collectLatest { data ->
                currentExpenseList.update { data }

                val expenseList = currentExpenseList.value.filter { !it.isIncome }
                val incomeList = currentExpenseList.value.filter { it.isIncome }

                val dataPoints = filterExpenseByDate(
                    items = expenseList
                )
                val incomeDataPoints = filterExpenseByDate(
                    items = incomeList
                )
                _state.update {
                    it.copy(
                        dataPoints =dataPoints,
                        incomeDataPoints = incomeDataPoints,
                        expenseSum = dataPoints.sumOf { it.y.toLong() },
                        incomeSum = incomeDataPoints.sumOf { it.y.toLong() }
                    )
                }


            }
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        _state.value
    )

    fun onEvent(event: AnalyticsEvent){
        when(event){
            is AnalyticsEvent.OnDateFilterChange -> {
                val expenseList = currentExpenseList.value.filter { !it.isIncome }
                val incomeList = currentExpenseList.value.filter { it.isIncome }
                val dataPoints = filterExpenseByDate(
                    dateFilter = event.dateFilter,
                    items = expenseList
                )
                val incomeDataPoints = filterExpenseByDate(
                    dateFilter = event.dateFilter,
                    items = incomeList
                )
                _state.update {
                    it.copy(
                        dateFilter = event.dateFilter,
                        dataPoints = dataPoints,
                        incomeDataPoints = incomeDataPoints,
                        selectedDataPoint = null,
                        incomeSelectedDataPoint = null,
                        expenseSum = dataPoints.sumOf { it.y.toLong() },
                        incomeSum = incomeDataPoints.sumOf {it.y.toLong() }
                    )
                }
            }
            is AnalyticsEvent.OnSelectDataPoint  -> {
                _state.update {
                    it.copy(
                        selectedDataPoint = event.dataPoint
                    )
                }
            }
            is AnalyticsEvent.OnIncomeSelectDataPoint  -> {
                _state.update {
                    it.copy(
                        incomeSelectedDataPoint = event.dataPoint
                    )
                }
            }
            is AnalyticsEvent.OnMoneyManagerTypeChange -> {
                _state.update {
                    it.copy(
                        moneyManagerTypeFilter = event.moneyManagerTypeFilter
                    )
                }
            }
            else -> Unit
        }
    }

    private fun filterExpenseByDate(
        dateFilter: DateFilter = DateFilter.SEVEN_DAYS,
        items: List<Expense> = emptyList()
    ): List<DataPoint>{
        return when(dateFilter){
            DateFilter.ALL -> {
                val filteredItems = items
                    .sortedBy { it.timestamp }
                    .groupBy { it.timestamp.toStringDateMByTimestamp() }

                filteredItems.map {
                    DataPoint(
                        x = it.key.length.toFloat(),
                        y = it.value.sumOf { it.cost }.toFloat(),
                        xLabel = it.key
                    )
                }
            }
            else -> {
                val filteredItems = items.filter {
                    it.timestamp in getTimestampRangeByDateFilter(dateFilter)
                }
                    .sortedBy { it.timestamp }
                    .groupBy {
                        when(dateFilter){
                            DateFilter.SEVEN_DAYS   -> it.timestamp.toStringDateMDByTimestamp()
                            DateFilter.ONE_MONTH   -> it.timestamp.toStringDateMDByTimestamp()
                            DateFilter.THREE_MONTHS -> it.timestamp.toStringDateMByTimestamp()
                            DateFilter.SIX_MONTHS -> it.timestamp.toStringDateMByTimestamp()
                            DateFilter.ONE_YEAR     -> it.timestamp.toStringDateMByTimestamp()
                            else -> it.timestamp.toStringDateByTimestamp()
                        }
                    }

                filteredItems.map {
                    DataPoint(
                        x = it.key.length.toFloat(),
                        y = it.value.sumOf { it.cost }.toFloat(),
                        xLabel = it.key
                    )
                }
            }
        }


    }

    private fun getTimestampRangeByDateFilter(
        dateFilter: DateFilter = DateFilter.SEVEN_DAYS
    ): LongRange{
        val now = DateConverter.getNowDate()
        val nowDate = LocalDate(
            year = now.year,
            monthNumber = now.monthNumber,
            dayOfMonth = now.dayOfMonth + 1
        )
        return when(dateFilter){
            DateFilter.SEVEN_DAYS   -> {
                val startDayOfMonth = nowDate.toEpochMilliseconds()
                val sevenDaysBefore = nowDate.minus(
                    DatePeriod(
                        days = 7)
                ).toEpochMilliseconds()
                LongRange(sevenDaysBefore , startDayOfMonth)
            }
            DateFilter.ONE_MONTH   -> {
                val pair = DateConverter.getMonthStartAndEndTime(
                    year = nowDate.year,
                    month = nowDate.monthNumber
                )
                LongRange(pair.first , pair.second)
            }
            DateFilter.THREE_MONTHS -> {
                val startDayOfMonth = nowDate.toEpochMilliseconds()
                val minus2MonthsLocalDate = nowDate.minus(DatePeriod(months = 3))
                val endDay = LocalDate(
                    year = minus2MonthsLocalDate.year,
                    monthNumber = minus2MonthsLocalDate.monthNumber,
                    dayOfMonth = 1
                ).toEpochMilliseconds()
                LongRange(endDay , startDayOfMonth)
            }
            DateFilter.SIX_MONTHS -> {
                val startDayOfMonth = nowDate.toEpochMilliseconds()
                val minus2MonthsLocalDate = nowDate.minus(DatePeriod(months = 6))
                val endDay = LocalDate(
                    year = minus2MonthsLocalDate.year,
                    monthNumber = minus2MonthsLocalDate.monthNumber,
                    dayOfMonth = 1
                ).toEpochMilliseconds()
                LongRange(endDay , startDayOfMonth)
            }
            DateFilter.ONE_YEAR     -> {
                val startDayOfMonth = nowDate.toEpochMilliseconds()
                val endDate = LocalDate(
                    year = now.year,
                    monthNumber = 1,
                    dayOfMonth = 1
                ).toEpochMilliseconds()

                LongRange(endDate , startDayOfMonth)
            }
            DateFilter.ALL          -> {
                LongRange(1 , 1)
            }
        }

    }
}

