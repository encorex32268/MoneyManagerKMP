package core.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class SpendingLimitEntity(
    @PrimaryKey
    var id: ObjectId = BsonObjectId(),
    var year: Int,
    var month: Int,
    var limit: Long
): RealmObject {
    constructor() : this(year = 0 , month = 0 ,limit = 0L)
}