package feature.chart.presentation.chartdetail

import androidx.lifecycle.ViewModel
import feature.core.domain.model.Expense
import feature.core.domain.model.Type
import feature.core.presentation.date.DateConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    val state = _state.asStateFlow()

    init {
        val dataGroup = items
            .sortedByDescending {
                it.timestamp
            }
            .groupBy {
                DateConverter.getDayStringDefault(it.timestamp)
            }
            .toList()

        _state.update {
            it.copy(
                items = dataGroup,
                total = items.sumOf { it.cost },
                type = type
            )
        }
    }
}