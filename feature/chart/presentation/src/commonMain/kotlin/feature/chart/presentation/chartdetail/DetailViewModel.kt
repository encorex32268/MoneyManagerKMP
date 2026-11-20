package feature.chart.presentation.chartdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import core.domain.model.Expense
import core.domain.model.Type
import core.presentation.date.DateConverter
import core.presentation.date.toDayString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

class DetailViewModel(
    private val items: List<Expense>,
    private val type: Type
): ViewModel() {

    private val _state = MutableStateFlow(
        DetailState(
            type = type
        )
    )
    val state =
        _state.onStart {
            val dataGroup = items
                .sortedByDescending {
                    it.timestamp
                }
                .groupBy {
                    it.timestamp.toDayString()
                }
                .toList()
            _state.update {
                it.copy(
                    items = dataGroup,
                    total = items.sumOf { it.cost },
                    type = type
                )
            }
        }.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000L),
            _state.value
        )
}