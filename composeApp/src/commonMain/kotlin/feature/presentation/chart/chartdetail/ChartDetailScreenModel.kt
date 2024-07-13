package feature.presentation.chart.chartdetail

import cafe.adriel.voyager.core.model.ScreenModel
import feature.core.domain.model.Expense
import feature.core.presentation.date.DateConverter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChartDetailScreenModel: ScreenModel {

    private val _state = MutableStateFlow(ChartDetailScreenState())
    val state = _state.asStateFlow()

    fun setupDetail(
        items: List<Expense> = emptyList()
    ){
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
                typeId = if (items.isEmpty()) 0 else items[0].typeId
            )
        }
    }

}