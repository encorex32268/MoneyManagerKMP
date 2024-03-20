package feature.core.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class CategoryEntity(
    @PrimaryKey
    var id: ObjectId = ObjectId(),
    var nameResId: Int?,
    var name: String,
    var resIdString: String,
    var typeId: Int,
): RealmObject {
    constructor() : this(
        nameResId = null , name = "",  resIdString = "", typeId = 0
    )
}
