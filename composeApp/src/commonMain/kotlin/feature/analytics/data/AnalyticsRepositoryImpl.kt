package feature.analytics.data

import feature.analytics.domain.AnalyticsRepository
import feature.core.data.MongoDB
import feature.core.domain.model.Expense
import feature.core.domain.repository.ExpenseRepository
import kotlinx.coroutines.flow.Flow

class AnalyticsRepositoryImpl(
    private val expenseRepository: ExpenseRepository
) : AnalyticsRepository {

    override fun getAllExpense(): Flow<List<Expense>> {
        return expenseRepository.getAll()
    }
}