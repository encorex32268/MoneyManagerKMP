package core.presentation.date

import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime

object DateConverter {

    fun getNowDate(): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(timeZone)
        return localDateTime
    }

    fun getNowDateTimestamp(): Long {
        return getNowDate().toTimestamp()
    }

    fun getMonthStartAndEndTime(
        year: Int?= null,
        month: Int? = null
    ): Pair<Long,Long>{
        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(timeZone)
        val startDayOfMonth = LocalDate(
            year = year?:localDateTime.year,
            monthNumber = month?:localDateTime.monthNumber,
            dayOfMonth = 1,
        )
        val endOfDayMonth = startDayOfMonth
            .plus(1, DateTimeUnit.MONTH) - DatePeriod(days = 1)


        val startTime = LocalDateTime(
            year = startDayOfMonth.year,
            monthNumber = startDayOfMonth.monthNumber,
            dayOfMonth = startDayOfMonth.dayOfMonth,
            hour = 0,
            minute = 0,
            second = 0,
            nanosecond = 0
        )

        val endTime = LocalDateTime(
            year = endOfDayMonth.year,
            monthNumber = endOfDayMonth.monthNumber,
            dayOfMonth = endOfDayMonth.dayOfMonth,
            hour = 23,
            minute = 59,
            second = 59,
            nanosecond = 999_999_999
        )
        return Pair(
            first = startTime.toInstant(timeZone).toEpochMilliseconds(),
            second = endTime.toInstant(timeZone).toEpochMilliseconds()
        )
    }
}

fun LocalDate.toEpochMilliseconds(): Long {
    val timeZone = TimeZone.currentSystemDefault()
    return this.atStartOfDayIn(timeZone).toEpochMilliseconds()
}

fun Long.toDayString(): String {
    val timezone = TimeZone.currentSystemDefault()
    val localDateTime = Instant.fromEpochMilliseconds(this).toLocalDateTime(timezone)
    val dayOfMonth = localDateTime.dayOfMonth
    val dayOfWeek = localDateTime.dayOfWeek
    return "$dayOfMonth $dayOfWeek"
}


fun Long.toLocalDateTime(): LocalDateTime {
    val timezone = TimeZone.currentSystemDefault()
    return Instant.fromEpochMilliseconds(this).toLocalDateTime(timezone)
}


fun Long.toStringDateByTimestamp(): String{
    val localDateTime = this.toLocalDateTime()
    return "${localDateTime.monthNumber}/${localDateTime.dayOfMonth} \n ${localDateTime.year}"

}

fun Long.toStringDateYMDByTimestamp(): String{
    val localDateTime = this.toLocalDateTime()
    return " ${localDateTime.year}/${localDateTime.monthNumber}/${localDateTime.dayOfMonth}"

}
fun Long.toStringDateMDByTimestamp(): String{
    val localDateTime = this.toLocalDateTime()
    return "${localDateTime.monthNumber}/${localDateTime.dayOfMonth}"

}

fun Long.toStringDateMByTimestamp(): String{
    val localDateTime = this.toLocalDateTime()
    return "${localDateTime.monthNumber} \n" +
            " ${localDateTime.year}"

}

fun LocalDateTime?.toTimestamp(): Long{
    val timezone = TimeZone.currentSystemDefault()
    return this?.toInstant(timezone)?.toEpochMilliseconds()?:0L
}