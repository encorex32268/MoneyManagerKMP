package core.presentation.date

import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDateTime
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.friday
import moneymanagerkmp.composeapp.generated.resources.monday
import moneymanagerkmp.composeapp.generated.resources.saturday
import moneymanagerkmp.composeapp.generated.resources.sunday
import moneymanagerkmp.composeapp.generated.resources.thursday
import moneymanagerkmp.composeapp.generated.resources.tuesday
import moneymanagerkmp.composeapp.generated.resources.wednesday
import org.jetbrains.compose.resources.StringResource

fun LocalDateTime.toDayOfWeekStringResource(): StringResource {
    return when (this.dayOfWeek) {
        DayOfWeek.MONDAY -> Res.string.monday
        DayOfWeek.TUESDAY -> Res.string.tuesday
        DayOfWeek.WEDNESDAY -> Res.string.wednesday
        DayOfWeek.THURSDAY -> Res.string.thursday
        DayOfWeek.FRIDAY -> Res.string.friday
        DayOfWeek.SATURDAY -> Res.string.saturday
        DayOfWeek.SUNDAY -> Res.string.sunday
        else -> Res.string.sunday
    }
}
