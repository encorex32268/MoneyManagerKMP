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
    private val nowDate = getNowDate()
    private val _state = MutableStateFlow(AnalyticsState())
    val state = _state.onStart {
        viewModelScope.launch {
            repository.getAllExpense().collectLatest { data ->
                currentExpenseList.update { data.filter { !it.isIncome } }
                val expenseList = currentExpenseList.value.filter { !it.isIncome }
                val dataPoints = filterExpenseByDate(items = expenseList)
                val nowDate = getNowDate()
                val nowYear = nowDate.year
                val nowMonth = nowDate.monthNumber
                _state.update {
                    it.copy(
                        dataPoints =dataPoints,
                        expenseSum = dataPoints.sumOf { it.y.toLong() },
                        nowDateYear = nowYear.toString(),
                        nowDateMonth = nowMonth.toString(),
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
                val dataPoints = filterExpenseByDate(
                    dateFilter = event.dateFilter,
                    items = currentExpenseList.value
                )
                _state.update {
                    it.copy(
                        dateFilter = event.dateFilter,
                        dataPoints = dataPoints,
                        selectedDataPoint = null,
                        expenseSum = dataPoints.sumOf { it.y.toLong() },
                        nowDateYear = nowDate.year.toString(),
                        nowDateMonth = nowDate.monthNumber.toString()
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
            is AnalyticsEvent.OnDatePick -> {
                if (state.value.dateFilter != DateFilter.ONE_MONTH) return
                val nowDateYear = event.year ?: nowDate.year
                val nowDateMonth = event.month ?: nowDate.monthNumber
                val (startTime,endTime) = DateConverter.getMonthStartAndEndTime(
                    year =  event.year,
                    month = event.month
                )

                val filteredItems = currentExpenseList.value.filter {
                    it.timestamp in LongRange(startTime , endTime)
                }
                    .sortedBy { it.timestamp }
                    .groupBy {
                        it.timestamp.toStringDateMDByTimestamp()
                    }
                val dataPoints = filteredItems.map {
                    DataPoint(
                        x = it.key.length.toFloat(),
                        y = it.value.sumOf { it.cost }.toFloat(),
                        xLabel = it.key
                    )
                }
                _state.update {
                    it.copy(
                        nowDateYear = nowDateYear.toString(),
                        nowDateMonth = nowDateMonth.toString(),
                        dataPoints = dataPoints,
                        expenseSum = dataPoints.sumOf { it.y.toLong() },
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
            DateFilter.ONE_MONTH -> {
                val nowDate = getNowDate()
                val (startTime,endTime) = DateConverter.getMonthStartAndEndTime(
                    year =  nowDate.year,
                    month = nowDate.monthNumber
                )
                val filteredItems = currentExpenseList.value.filter {
                    it.timestamp in LongRange(startTime , endTime)
                }
                    .sortedBy { it.timestamp }
                    .groupBy {
                        it.timestamp.toStringDateMDByTimestamp()
                    }
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
                val sevenDaysBefore = nowDate.minus(DatePeriod(days = 7)).toEpochMilliseconds()
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

