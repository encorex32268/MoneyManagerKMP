package feature.home.presentation

import AppLogger
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.model.SpendingLimit
import core.domain.repository.SpendingLimitRepository
import core.presentation.date.DateConverter
import core.presentation.date.DateConverter.getNowDate
import core.presentation.date.toDayString
import core.presentation.model.MonthYear
import feature.home.domain.mapper.toExpenseUi
import feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

@OptIn(ExperimentalCoroutinesApi::class)
class HomeViewModel(
    private val repository: HomeRepository,
    private val spendingLimitRepository: SpendingLimitRepository
) : ViewModel() {

    private var currentSpendingLimit: SpendingLimit? = null
    private val selectedMonthYear = MutableStateFlow(MonthYear())

    val state: StateFlow<HomeState> = selectedMonthYear
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
            ) { spendingLimit, data ->
                currentSpendingLimit = spendingLimit
                val expenseItems = data.filterNot { it.isIncome }
                val totalExpense = expenseItems.sumOf { it.cost }
                val dataGroup = expenseItems
                    .map { it.toExpenseUi() }
                    .sortedByDescending { it.timestamp }
                    .groupBy { it.timestamp.toDayString() }
                    .toList()
                AppLogger.d("HomeViewModel", "$dataGroup")
                HomeState(
                    totalExpense = totalExpense,
                    expenseLimit = spendingLimit?.limit ?: 0L,
                    nowDateYear = targetYear.toString(),
                    nowDateMonth = targetMonth.toString(),
                    nowDateDayOfMonth = nowDate.dayOfMonth.toString(),
                    items = dataGroup
                )
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = HomeState()
        )

    fun onAction(event: HomeAction) {
        when (event) {
            is HomeAction.OnDatePick -> {
                selectedMonthYear.value = MonthYear(year = event.year, month = event.month)
            }
            is HomeAction.OnSpendingLimitChange -> onSpendingLimitChange(event.expenseLimitText)
            HomeAction.OnExpenseLimitClick -> Unit
            HomeAction.OnGotoAddScreen -> Unit
            is HomeAction.OnGotoEditScreen -> Unit
        }
    }

    private fun onSpendingLimitChange(expenseLimitText: String) {
        val expenseLimit = expenseLimitText.toLongOrNull() ?: return
        viewModelScope.launch {
            val existing = currentSpendingLimit
            if (existing != null) {
                spendingLimitRepository.update(
                    spendingLimit = existing.copy(limit = expenseLimit)
                )
            } else {
                val nowDate = getNowDate()
                val targetYear = selectedMonthYear.value.year ?: nowDate.year
                val targetMonth = selectedMonthYear.value.month ?: nowDate.monthNumber
                spendingLimitRepository.insert(
                    SpendingLimit(
                        limit = expenseLimit,
                        year = targetYear,
                        month = targetMonth
                    )
                )
            }
        }
    }
}
