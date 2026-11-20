package core.data.repository

import AppLogger
import core.data.database.dao.TypeDao
import core.data.database.mapper.toRoomTypeAndCategories
import core.data.database.mapper.toType
import core.domain.model.Type
import core.domain.repository.TypeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TypeRepositoryImpl(
    private val typeDao: TypeDao
) : TypeRepository {

    override fun getTypes(): Flow<List<Type>> {
        return typeDao.getTypes().map { list ->
            list.map { it.toType() }
        }
    }

    override suspend fun update(type: Type) {
        val (typeEntity, categoryEntities) = type.toRoomTypeAndCategories()
        typeDao.upsertTypeWithCategories(typeEntity, categoryEntities)
    }

    override suspend fun insert(type: Type) {
        val (typeEntity, categoryEntities) = type.toRoomTypeAndCategories()
        typeDao.upsertTypeWithCategories(typeEntity, categoryEntities)
    }

    override suspend fun delete(type: Type) {
        typeDao.deleteTypeById(type.id)
    }

    override suspend fun updateAllSortedTypes(types: List<Type>) {
        typeDao.upsertAllTypesWithCategories(
            types.map { it.toRoomTypeAndCategories() }
        )
    }

    override suspend fun restore(types: List<Type>) {
        val dbTypes = typeDao.getTypesOnce().map { it.toType() }
        val restoreTimestamps = types.map { it.typeIdTimestamp }.toSet()
        val dbTimestamps = dbTypes.map { it.typeIdTimestamp }.toSet()

        val existsTypes = dbTypes.map { type ->
            if (type.typeIdTimestamp in restoreTimestamps) {
                val restoreType = types.find { it.typeIdTimestamp == type.typeIdTimestamp }
                AppLogger.d("TypeRepository Restore", "RestoreType: $restoreType")
                val dbTypeCategories = type.categories
                val restoreCategories = restoreType?.categories ?: emptyList()
                type.copy(
                    categories = (dbTypeCategories + restoreCategories).distinct()
                )
            } else {
                type
            }
        }

        val notExistsTypes = types.filterNot { it.typeIdTimestamp in dbTimestamps }
        val finalTypes = existsTypes + notExistsTypes

        typeDao.upsertAllTypesWithCategories(
            finalTypes.map { it.toRoomTypeAndCategories() }
        )
    }
}
