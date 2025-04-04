package feature.home.domain.util

import androidx.compose.runtime.Composable
import kotlinx.datetime.DayOfWeek
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


@OptIn(ExperimentalResourceApi::class)
@Composable
fun displayDayString(text: DayOfWeek) : String {
    return when(text){
        DayOfWeek.MONDAY -> stringResource(Res.string.monday)
        DayOfWeek.TUESDAY -> stringResource(Res.string.tuesday)
        DayOfWeek.WEDNESDAY -> stringResource(Res.string.wednesday)
        DayOfWeek.THURSDAY -> stringResource(Res.string.thursday)
        DayOfWeek.FRIDAY -> stringResource(Res.string.friday)
        DayOfWeek.SATURDAY -> stringResource(Res.string.saturday)
        DayOfWeek.SUNDAY -> stringResource(Res.string.sunday)
        else -> ""
    }
}