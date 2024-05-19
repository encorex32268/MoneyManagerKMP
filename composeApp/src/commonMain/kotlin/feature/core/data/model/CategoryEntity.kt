package feature.core.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CategoryEntity(
    @PrimaryKey
    var id: ObjectId = ObjectId(),
    var categoryId: Int,
    var typeId: Int,
): RealmObject {
    constructor() : this(
       id = ObjectId() , typeId = 0 , categoryId = 0
    )
}
