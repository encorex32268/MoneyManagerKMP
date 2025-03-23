package core.data.repository

import core.data.model.SpendingLimitEntity
import core.domain.model.SpendingLimit
import core.domain.repository.SpendingLimitRepository
import core.domain.mapper.toSpendLimitEntity
import core.domain.mapper.toSpendingLimit
import io.realm.kotlin.Realm
import io.realm.kotlin.ext.query
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SpendingLimitRepositoryImpl(
    private val realm: Realm
): SpendingLimitRepository {

    override fun getAll(): Flow<List<SpendingLimit>> {
        return realm.query<SpendingLimitEntity>().find().asFlow().map {
            if (it.list.isEmpty()){
                emptyList()
            }else{
                it.list.toList().map { spendingLimitEntity ->
                    spendingLimitEntity.toSpendingLimit()
                }
            }
        }
    }

    override fun getSpendingLimit(year: Int , month: Int): Flow<SpendingLimit?> {
        return realm.query(
            clazz = SpendingLimitEntity::class,
            query = "year == $0 AND month == $1",year,month
        ).find().asFlow().map {
            if (it.list.isEmpty()){
                null
            }else{
                it.list.first().toSpendingLimit()
            }
        }
    }

    override suspend fun insert(spendingLimit: SpendingLimit) {
        realm.write {
            copyToRealm(
                instance = spendingLimit.toSpendLimitEntity()
            )
        }
    }

    override suspend fun update(spendingLimit: SpendingLimit) {
        realm.write {
            try {
                val queriedTask = query<SpendingLimitEntity>(
                    query = "id == $0", spendingLimit.id
                )
                    .find()
                    .first()
                queriedTask.apply {
                    year = spendingLimit.year
                    month = spendingLimit.month
                    limit = spendingLimit.limit
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println(e)
            }
        }
    }

    override suspend fun delete(spendingLimit: SpendingLimit) {
        realm.write {
            val queriedSpendingLimitEntity = query<SpendingLimitEntity>(
                query = "id == $0", spendingLimit.id
            )
                .first()
                .find()
            if (queriedSpendingLimitEntity == null) return@write
            findLatest(queriedSpendingLimitEntity)?.let { currentSpendingLimit ->
                delete(currentSpendingLimit)
            }
        }
    }
}