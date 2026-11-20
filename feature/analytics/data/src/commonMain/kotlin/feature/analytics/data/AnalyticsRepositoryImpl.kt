package feature.analytics.data

import core.domain.model.Expense
import core.domain.repository.ExpenseRepository
import feature.analytics.domain.AnalyticsRepository
import kotlinx.coroutines.flow.Flow

class AnalyticsRepositoryImpl(
    private val expenseRepository: ExpenseRepository
) : AnalyticsRepository {

    override fun getAllExpense(): Flow<List<Expense>> {
        return expenseRepository.getAll()
    }
}