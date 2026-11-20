package core.domain.repository

import core.domain.model.Type
import kotlinx.coroutines.flow.Flow

interface TypeRepository {
    fun getTypes(): Flow<List<Type>>
    suspend fun update(type: Type)
    suspend fun insert(type: Type)
    suspend fun delete(type: Type)
    suspend fun updateAllSortedTypes(types: List<Type>)
    suspend fun restore(types: List<Type>)
}