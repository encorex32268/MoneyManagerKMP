package feature.chart.data.repository

import feature.chart.domain.repository.ChartRepository
import feature.core.domain.model.Expense
import feature.core.domain.repository.ExpenseRepository
import feature.core.domain.repository.TypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class ChartRepositoryImpl(
    private val expenseRepository: ExpenseRepository,
    private val typeRepository: TypeRepository
): ChartRepository {

    override suspend fun getExpenseByTime(
        startTimeOfMonth: Long,
        endTimeOfMonth: Long
    ): Flow<List<Expense>> {

        val getExpenseFlow = expenseRepository.getExpenseByTime(
            startTimeOfMonth = startTimeOfMonth,
            endTimeOfMonth = endTimeOfMonth
        )
        val getTypeFlow = typeRepository.getTypes()

        return combine(getExpenseFlow , getTypeFlow ){ expenseList , typeList ->
            val isShowType = typeList.filter { it.isShow }.map { it.typeIdTimestamp }
            expenseList.filter {
                it.typeId in isShowType
            }
        }

    }
}