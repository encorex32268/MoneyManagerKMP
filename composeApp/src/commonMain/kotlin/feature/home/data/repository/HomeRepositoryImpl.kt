package feature.home.data.repository

import core.domain.mapper.toExpenseEntity
import core.domain.model.Expense
import core.domain.repository.ExpenseRepository
import core.domain.repository.TypeRepository
import feature.home.domain.mapper.toExpenseUi
import feature.home.domain.repository.HomeRepository
import feature.home.presentation.model.ExpenseUi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine

class HomeRepositoryImpl(
    private val expenseRepository: ExpenseRepository,
    private val typeRepository: TypeRepository
): HomeRepository {

    override fun getExpenseByTime(
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
           }.map { expense ->
               val findType = typeList.find {
                   it.typeIdTimestamp == expense.typeId
               }
               expense.copy(
                   type = findType
               )
           }
        }

    }

}