package feature.core.domain.model

import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

data class Expense(
    val id: ObjectId = BsonObjectId(),
    val categoryId: Int,
    val typeId: Int,
    val description: String,
    val isIncome: Boolean=false,
    val cost: Long = 0,
    val timestamp: Long = 0
){
    private fun getStringTime() : String {
        val instant = Instant.fromEpochMilliseconds(timestamp)
        val formatter = DateTimeComponents.Format {
            year()
            chars("-")
            monthNumber()
            chars("-")
            dayOfMonth()
            chars(" ")
            hour()
            chars(":")
            minute()
            chars(":")
            second()
        }
        return instant.format(formatter)
    }


}
