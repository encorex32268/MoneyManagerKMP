package core.data.model

import io.realm.kotlin.ext.realmListOf
import io.realm.kotlin.types.RealmList
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.RealmUUID
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

class TypeEntity(
    @PrimaryKey
    var id: ObjectId = BsonObjectId(),
    var typeIdHex: String = id.toHexString(),
    var typeIdTimestamp: Long,
    var name: String,
    var colorArgb: Int,
    var order: Int,
    var isShow: Boolean = true,
    var categories: RealmList<CategoryEntity>
): RealmObject {

    constructor(): this(
        typeIdTimestamp = 0 , name = "" , colorArgb = 0 , order = 0 , categories = realmListOf()
    )
}