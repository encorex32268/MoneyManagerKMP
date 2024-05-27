package feature.core.domain.model

import dev.icerock.moko.parcelize.IgnoredOnParcel
import dev.icerock.moko.parcelize.Parcelable
import dev.icerock.moko.parcelize.Parcelize
import kotlinx.datetime.Instant
import kotlinx.datetime.format
import kotlinx.datetime.format.DateTimeComponents
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import org.mongodb.kbson.BsonObjectId
import org.mongodb.kbson.ObjectId

@Serializable
@Parcelize
data class Expense(
    @IgnoredOnParcel @Transient val id: ObjectId = ObjectId(),
    val idString: String = id.toHexString(),
    val categoryId: Int,
    val typeId: Int,
    val description: String,
    val isIncome: Boolean=false,
    val cost: Long = 0,
    val timestamp: Long = 0
): Parcelable
