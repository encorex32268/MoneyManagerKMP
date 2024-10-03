package feature.home.data.repository

import feature.core.domain.model.Expense
import feature.core.domain.repository.ExpenseRepository
import feature.core.domain.repository.TypeRepository
import feature.home.domain.repository.HomeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine

class HomeRepositoryImpl(
    private val expenseRepository: ExpenseRepository,
    private val typeRepository: TypeRepository
): HomeRepository {

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