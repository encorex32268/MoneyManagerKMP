package feature.core.data.model

import feature.core.domain.model.Category
import io.realm.kotlin.types.RealmObject
import io.realm.kotlin.types.annotations.Ignore
import io.realm.kotlin.types.annotations.PrimaryKey
import org.mongodb.kbson.ObjectId

class ExpenseEntity(
    @PrimaryKey
    var id: ObjectId = ObjectId(),
    @Ignore
    var category: Category?,
    var description: String,
    var isIncome: Boolean=false,
    var cost: Long = 0,
    var timestamp: Long = 0
) : RealmObject{
    constructor() : this(
        category=null,description = "",isIncome = false, cost = 0 , timestamp =0
    )
}
