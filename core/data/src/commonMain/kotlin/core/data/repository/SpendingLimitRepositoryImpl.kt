package core.data.repository

import AppLogger
import core.data.database.dao.SpendingLimitDao
import core.data.database.mapper.toRoomEntity
import core.data.database.mapper.toSpendingLimit
import core.domain.model.SpendingLimit
import core.domain.repository.SpendingLimitRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpendingLimitRepositoryImpl(
    private val spendingLimitDao: SpendingLimitDao
) : SpendingLimitRepository {

    override fun getAll(): Flow<List<SpendingLimit>> {
        return spendingLimitDao.getAll().map { list ->
            list.map { it.toSpendingLimit() }
        }
    }

    override fun getSpendingLimit(year: Int, month: Int): Flow<SpendingLimit?> {
        return spendingLimitDao.getSpendingLimit(year = year, month = month).map { entity ->
            entity?.toSpendingLimit()
        }
    }

    override suspend fun insert(spendingLimit: SpendingLimit) {
        spendingLimitDao.upsert(spendingLimit.toRoomEntity())
    }

    override suspend fun update(spendingLimit: SpendingLimit) {
        spendingLimitDao.upsert(spendingLimit.toRoomEntity())
    }

    override suspend fun delete(spendingLimit: SpendingLimit) {
        spendingLimitDao.deleteById(spendingLimit.id)
    }

    override suspend fun restore(spendingLimits: List<SpendingLimit>) {
        AppLogger.d("SpendingLimit", "spending$spendingLimits")
        if (spendingLimits.isNotEmpty()) {
            spendingLimitDao.upsertAll(spendingLimits.map { it.toRoomEntity() })
        }
    }
}
