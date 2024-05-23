package feature.core.data.model

import feature.core.domain.model.Category
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ExpenseEntity(
    @PrimaryKey
    var id: ObjectId = ObjectId(),
    var categoryId: Int,
    var typeId: Int,
    var description: String,
    var isIncome: Boolean,
    var cost: Long,
    var timestamp: Long,
) : RealmObject{
    constructor() : this(
        categoryId = 0,
        typeId = 0,
        description = "",
        isIncome = false,
        cost = 0 , timestamp =0
    )
}
