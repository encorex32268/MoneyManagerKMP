package core.data.model

import io.realm.kotlin.types.RealmObject


class CategoryEntity(
    var id: Int,
    var name: String,
    var order: Int,
    var typeId: Long,
): RealmObject {
    constructor(): this(
        id = 0 , name ="" , order= 0 , typeId =0L
    )
}

