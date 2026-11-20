@file:OptIn(ExperimentalResourceApi::class)

package feature.home.presentation.add.components

import app.presentation.LocalDarkLightMode
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DisplayMode
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.navigation.NavigationLayoutType
import core.ui.bgColor
import core.ui.calculateCalendarDarkContainerColor
import core.ui.calculateCalendarLightContainerColor
import core.ui.calculateDoneDarkContainerColor
import core.ui.calculateDoneLightContainerColor
import core.ui.calculateRemoveDarkContainerColor
import core.ui.calculateRemoveLightContainerColor
import core.ui.highlightColor
import feature.home.presentation.add.AddEvent
import feature.home.presentation.add.AddState
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_done
import moneymanagerkmp.composeapp.generated.resources.baseline_remove
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import toDateString

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun CalculateLayout(
    modifier: Modifier = Modifier,
    onEvent: (AddEvent) -> Unit = {},
    month: String = "",
    day: String = "",
    state: AddState,
    numberButtonAspectRatio: Float = 1.25f,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION
){
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = when(navigationLayoutType){
            NavigationLayoutType.BOTTOM_NAVIGATION -> {
                DisplayMode.Picker
            }
            else -> {
                DisplayMode.Input
            }
        }
    )
    val isShowDialog = rememberSaveable { mutableStateOf(false) }
    if (isShowDialog.value) {
        DatePickerDialog(
            onDismissRequest = { isShowDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(
                            AddEvent.OnSelectedDate(
                            timestamp = datePickerState.selectedDateMillis?:0L)
                        )
                        isShowDialog.value = false
                    }) {
                    Text(
                        text = stringResource(Res.string.dialog_ok_button),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp
                        )
                    )
                }
            },
            dismissButton = {
                TextButton(
                    onClick = { isShowDialog.value = false }) {
                    Text(
                        text = stringResource(Res.string.dialog_cancel_button),
                        style = MaterialTheme.typography.labelLarge.copy(
                            fontSize = 14.sp
                        )
                    )
                }
            }
        ) {
            DatePicker(
                state = datePickerState
            )
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ExpenseInfo(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            onValueChange = {
                onEvent(AddEvent.OnDescriptionChange(it))
            },
            categoryUi = state.categoryUi,
            description = state.description,
            cost = state.cost.toLongOrNull()?:0L
        )
        Spacer(modifier = Modifier.height(8.dp))
        CalculateKeyboard(
            onCalendarButtonClick = { isShowDialog.value = true },
            onEvent = onEvent,
            month = month,
            day = day,
            aspectRatio = numberButtonAspectRatio
        )
    }

}

@Composable
private fun CalculateKeyboard(
    onCalendarButtonClick : () -> Unit = {},
    month: String = "",
    day : String = "",
    aspectRatio: Float = 1.25f,
    onEvent: (AddEvent) -> Unit = {},
    isDarkMode: Boolean = LocalDarkLightMode.current
){
    val numberButtonModifier = remember(aspectRatio){
        Modifier.aspectRatio(aspectRatio)
    }
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ){
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NumberButton(
                modifier = numberButtonModifier, text = "7", onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "4", onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "1", onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "0", onEvent =  onEvent
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NumberButton(
                modifier = numberButtonModifier, text = "8", onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "5", onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "2", onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "00", onEvent =  onEvent
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NumberButton(
                modifier = numberButtonModifier, text = "9", onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "6", onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "3" , onEvent =  onEvent
            )
            NumberButton(
                modifier = numberButtonModifier, text = "000", onEvent =  onEvent
            )
        }
        val removeBackgroundColor = remember(isDarkMode){
            if (isDarkMode) calculateRemoveDarkContainerColor else calculateRemoveLightContainerColor
        }
        val calendarBackgroundColor = remember(isDarkMode){
            if (isDarkMode) calculateCalendarDarkContainerColor else calculateCalendarLightContainerColor
        }
        val doneBackgroundColor = remember(isDarkMode){
            if (isDarkMode) calculateDoneDarkContainerColor else calculateDoneLightContainerColor
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculateIconButton(
                modifier = numberButtonModifier,
                icon = vectorResource(Res.drawable.baseline_remove),
                contentDescription = "remove button",
                onClick = {
                    onEvent(AddEvent.OnDeleteTextClick)
                },
                backgroundColor = removeBackgroundColor
            )
            CalendarButton(
                modifier = Modifier.aspectRatio(aspectRatio / 2.0625f),
                month = month.toInt(),
                day = day.toInt(),
                onClick = onCalendarButtonClick,
                backgroundColor = calendarBackgroundColor,
                contentDescription = "calendar button"
            )
            CalculateIconButton(
                modifier = numberButtonModifier,
                icon = vectorResource(Res.drawable.baseline_done),
                contentDescription = "done button",
                onClick = {
                    onEvent(AddEvent.OnSaveClick)
                },
                backgroundColor = doneBackgroundColor
            )
        }

    }

}

@Composable
private fun CalculateIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    backgroundColor: Color = MaterialTheme.colorScheme.background,
    shape: Shape = RoundedCornerShape(24.dp),
    icon: ImageVector,
    contentDescription: String?=null
) {
    Box(
        modifier = modifier
            .clip(shape)
            .size(36.dp)
            .clickable {
                onClick()
            }
            .background(
                backgroundColor,
                shape
            )
        ,
        contentAlignment = Alignment.Center
    ){
        Icon(
            modifier = Modifier.size(24.dp),
            imageVector = icon,
            contentDescription = contentDescription
        )

    }
}




@Composable
private fun CalendarButton(
    modifier : Modifier = Modifier,
    month: Int = 1,
    day: Int = 6,
    onClick: () -> Unit = {},
    shape: Shape = RoundedCornerShape(16.dp),
    backgroundColor: Color,
    contentDescription: String? = null
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .size(36.dp)
            .clip(shape)
            .background(
                color = backgroundColor,
                shape = shape
            )
            .clickable { onClick() }
        ,
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = contentDescription,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = toDateString(month,day),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

}


@Composable
private fun NumberButton(
    modifier: Modifier = Modifier,
    text: String,
    onEvent: (AddEvent) -> Unit = {},
    shape: Shape = RoundedCornerShape(24.dp)
){
    Box(
        modifier = modifier
            .clip(shape)
            .clickable {
                onEvent(AddEvent.OnCostChange(text))
            }
            .background(
                color = MaterialTheme.colorScheme.highlightColor(),
                shape = shape
            )
        ,
        contentAlignment = Alignment.Center
    ){
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelMedium.copy(
                fontSize = 20.sp
            )
        )
    }
}