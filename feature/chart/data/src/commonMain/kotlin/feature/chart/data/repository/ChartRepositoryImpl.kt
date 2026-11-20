package feature.chart.data.repository

import feature.chart.domain.repository.ChartRepository
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ChartRepositoryImpl(
    private val expenseRepository: ExpenseRepository,
    private val typeRepository: TypeRepository
): ChartRepository {

    override fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<core.domain.model.chart.Chart>> {

        val getExpenseFlow = expenseRepository.getExpenseByTime(
            startTimeOfMonth = startTimeOfMonth,
            endTimeOfMonth = endTimeOfMonth
        )
        val getTypeFlow = typeRepository.getTypes()

        return combine(getExpenseFlow , getTypeFlow ){ expenseList , typeList ->
            val showTypes = typeList.filter { it.isShow }
            val chartItems = mutableListOf<core.domain.model.chart.Chart>()
            showTypes.forEach { type ->
               val expenses = expenseList.filter {
                   type.typeIdTimestamp == it.typeId && !it.isIncome
               }
               if (expenses.isNotEmpty()){
                   chartItems.add(
                       core.domain.model.chart.Chart(
                           type = type,
                           items = expenses
                       )
                   )
               }
            }
            chartItems

        }

    }
}