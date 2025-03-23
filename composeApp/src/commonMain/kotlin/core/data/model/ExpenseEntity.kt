package core.data.model

import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId


class ExpenseEntity(
    @PrimaryKey
    var id: ObjectId = ObjectId(),
    var categoryId: Int,
    var typeId: Long,
    var description: String,
    var isIncome: Boolean,
    var cost: Long,
    var timestamp: Long,
    var content: String = ""
) : RealmObject{
    constructor() : this(
        categoryId = 0,
        typeId = 0L,
        description = "",
        isIncome = false,
        cost = 0 , timestamp =0
    )
}
