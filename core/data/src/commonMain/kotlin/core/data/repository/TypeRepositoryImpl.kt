package core.data.repository

import core.data.model.TypeEntity
import core.domain.model.Type
import core.domain.repository.TypeRepository
import core.domain.mapper.toCategoryEntity
import core.domain.mapper.toType
import core.domain.mapper.toTypeEntity
import io.realm.kotlin.Realm
import io.realm.kotlin.UpdatePolicy
import io.realm.kotlin.ext.query
import io.realm.kotlin.ext.toRealmList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TypeRepositoryImpl(
    private val realm: Realm
): TypeRepository {
    override fun getTypes(): Flow<List<Type>> {
        return realm.query(
            clazz = TypeEntity::class
        ).find().asFlow().map {
            it.list.toList().map { typeEntity ->
                typeEntity.toType()
            }
        }
    }

    override suspend fun update(type: Type) {
        realm.write {
            try {
                val queriedTask = query<TypeEntity>(
                    query = "id == $0",type.id
                )
                    .find()
                    .first()
                queriedTask.apply {
                    name = type.name
                    colorArgb = type.colorArgb
                    order = type.order
                    isShow = type.isShow
                    categories = type.categories.map { it.toCategoryEntity() }.toRealmList()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                println(e)
            }
        }
    }

    override suspend fun insert(type: Type) {
        realm.write {
            copyToRealm(type.toTypeEntity())
        }
    }

    override suspend fun delete(type: Type) {
        realm.write {
            val queriedTypeEntity = query<TypeEntity>(
                query = "id == $0", type.id
            )
                .first()
                .find()
            if (queriedTypeEntity == null) return@write
            findLatest(queriedTypeEntity)?.let { currentType ->
                delete(currentType)
            }
        }
    }

    override suspend fun updateAllSortedTypes(types: List<Type>) {
        realm.write{
            types.map { type ->
                val queriedTask = query<TypeEntity>(
                    query = "id == $0",type.id
                )
                    .find()
                    .first()
                queriedTask.apply {
                    name = type.name
                    colorArgb = type.colorArgb
                    order = type.order
                    isShow = type.isShow
                    categories = type.categories.map { it.toCategoryEntity() }.toRealmList()
                }
            }
        }
    }

    override suspend fun restore(types: List<Type>) {
        realm.write{
            val dbTypes = this.query<TypeEntity>().find().map { it.toType() }
            val restoreTypes = types
            val restoreTimestamp = restoreTypes.map { it.typeIdTimestamp }
            val dbTimestamp = dbTypes.map { it.typeIdTimestamp }

            val existsTypes = dbTypes.map { type ->
                val dbTypeTimestamp = type.typeIdTimestamp
                if (dbTypeTimestamp in restoreTimestamp){
                    val restoreType = types.find { it.typeIdTimestamp == dbTypeTimestamp }
                    AppLogger.d("TypeRepository Restore" , "RestoreType: $restoreType")
                    val dbTypeCategories = type.categories
                    val restoreCategories = restoreType?.categories?:emptyList()

                    type.copy(
                        categories = (dbTypeCategories + restoreCategories).distinct()
                    )
                }else{
                    type
                }
            }

            val notExistsTypes = restoreTypes.filterNot { it.typeIdTimestamp in dbTimestamp }

            val lastTypes = existsTypes + notExistsTypes

            lastTypes.forEach {
                this.copyToRealm(
                    instance = it.toTypeEntity(),
                    updatePolicy = UpdatePolicy.ALL
                )
            }

        }
    }


}