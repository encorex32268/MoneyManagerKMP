package feature.home.presentation

import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import feature.core.domain.model.Category
import feature.core.domain.model.Expense
import feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeScreenModel(
    private val homeRepository: HomeRepository
) : ScreenModel {

    private val _state = MutableStateFlow(HomeState())
    val state = _state.asStateFlow()

    fun insertTestExpense(){
        screenModelScope.launch {
            homeRepository.insertExpense(
                Expense(
                    category = Category(
                        id = 0,
                        nameResId = 0,
                        name = "",
                        resIdString = "",
                        typeId = 0
                    ),
                    description = "Test",
                    isIncome = false,
                    cost = 100,
                    timestamp = 50
                )
            )
        }
    }

    fun getAll(){
        screenModelScope.launch {
            homeRepository.getExpenseByStartTimeAndEndTime(
                startTimeOfMonth = 0 , endTimeOfMonth = 100
            ).collectLatest {
                _state.update { state ->
                    state.copy(
                        items = it
                    )
                }
            }
        }
    }


}