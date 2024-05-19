package feature.core.presentation.date

import androidx.compose.runtime.Composable
import kotlinx.datetime.Clock
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Instant
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.atTime
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.friday
import moneymanagerkmp.composeapp.generated.resources.monday
import moneymanagerkmp.composeapp.generated.resources.saturday
import moneymanagerkmp.composeapp.generated.resources.sunday
import moneymanagerkmp.composeapp.generated.resources.thursday
import moneymanagerkmp.composeapp.generated.resources.tuesday
import moneymanagerkmp.composeapp.generated.resources.wednesday
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

object DateConverter {

    fun getNowDate(): LocalDateTime {
        val timeZone = TimeZone.currentSystemDefault()
        val now = Clock.System.now()
        val localDateTime = now.toLocalDateTime(timeZone)
        return localDateTime
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
            dayOfMonth = 1
        )
        val endOfDayMonth = startDayOfMonth
            .plus(1, DateTimeUnit.MONTH) - DatePeriod(days = 1)
        val endTime = LocalDateTime(
            year = endOfDayMonth.year,
            monthNumber = endOfDayMonth.monthNumber,
            dayOfMonth = endOfDayMonth.dayOfMonth,
            hour = 23,
            minute = 59,
            second = 59
        )
        return Pair(
            first = startDayOfMonth.atStartOfDayIn(timeZone).epochSeconds,
            second = endTime.toInstant(timeZone).epochSeconds
        )
    }



    @Suppress("IMPLICIT_CAST_TO_ANY")
    @OptIn(ExperimentalResourceApi::class)
    @Composable
    fun getDayStringWithLocalTranslate(
        timestamp: Long
    ): String{
        val timezone = TimeZone.currentSystemDefault()
        val localDateTime = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(timezone)
        val dayOfMonth = localDateTime.dayOfMonth
        val dayOfWeek = localDateTime.dayOfWeek
        val dayOfWeekDisplayName = when(dayOfWeek){
            DayOfWeek.MONDAY -> stringResource(Res.string.monday)
            DayOfWeek.TUESDAY -> stringResource(Res.string.tuesday)
            DayOfWeek.WEDNESDAY -> stringResource(Res.string.wednesday)
            DayOfWeek.THURSDAY -> stringResource(Res.string.thursday)
            DayOfWeek.FRIDAY -> stringResource(Res.string.friday)
            DayOfWeek.SATURDAY -> stringResource(Res.string.saturday)
            DayOfWeek.SUNDAY -> stringResource(Res.string.sunday)
            else -> Unit
        }
        return "$dayOfMonth $dayOfWeekDisplayName"
    }

    fun getDayStringDefault(
        timestamp: Long
    ): String{
        val timezone = TimeZone.currentSystemDefault()
        val localDateTime = Instant.fromEpochMilliseconds(timestamp).toLocalDateTime(timezone)
        val dayOfMonth = localDateTime.dayOfMonth
        val dayOfWeek = localDateTime.dayOfWeek
        return "$dayOfMonth $dayOfWeek"
    }

    fun getStringDateFromLong(): String {
        return ""
    }




}