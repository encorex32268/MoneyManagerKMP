@file:OptIn(ExperimentalResourceApi::class)

package feature.presentation.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.presentation.add.AddState
import feature.core.presentation.Texts
import feature.presentation.noRippleClick
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(
    ExperimentalMaterial3Api::class
)
@Composable
fun CalculateLayout(
    modifier: Modifier = Modifier,
    onValueChange: (String) -> Unit = {},
    onOkClick: () -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onDelete: () -> Unit = {},
    onDateSelected: (Long) -> Unit = {},
    month: String = "",
    day: String = "",
    state: AddState
){
    val datePickerState = rememberDatePickerState()
    val isShowDialog = rememberSaveable { mutableStateOf(false) }
    val keyboard = LocalSoftwareKeyboardController.current
    if (isShowDialog.value) {
        DatePickerDialog(
            colors = DatePickerDefaults.colors(
                containerColor = Color.White
            ),
            onDismissRequest = { isShowDialog.value = false },
            confirmButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    onClick = {
                        onDateSelected(datePickerState.selectedDateMillis?:0L)
                        isShowDialog.value = false
                    }) {
                    Text(stringResource(Res.string.dialog_ok_button))
                }
            },
            dismissButton = {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.Black,
                        containerColor = Color.Transparent
                    ),
                    onClick = { isShowDialog.value = false }) {
                    Text(stringResource(Res.string.dialog_cancel_button))
                }
            }
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = Color.White,
                    selectedDayContainerColor = Color.Black,
                    dayContentColor = Color.Black,
                    todayDateBorderColor = Color.Black,
                    todayContentColor = Color.Black,
                    selectedYearContainerColor = Color.Black,
                    selectedYearContentColor = Color.White,
                    yearContentColor = Color.Black,
                    dateTextFieldColors = TextFieldDefaults.colors(
                        focusedIndicatorColor = Color.Black,
                        focusedLabelColor = Color.Black,
                        disabledLabelColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedContainerColor = Color.White,
                        disabledContainerColor = Color.White,
                        unfocusedContainerColor = Color.White
                    )
                )
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
            onValueChange = onValueChange,
            categoryUi = state.categoryUi,
            description = state.description,
            cost = state.cost.toLongOrNull()?:0L
        )
        Spacer(modifier = Modifier.height(8.dp))
        CalculateKeyboard(
            onCalendarButtonClick = { isShowDialog.value = true },
            onDelete = onDelete,
            onOkClick = onOkClick,
            onItemClick = onItemClick,
            month = month,
            day = day

        )
    }

}

@Composable
private fun CalculateKeyboard(
    onOkClick : () -> Unit = {},
    onItemClick: (String) -> Unit = {},
    onDelete : () -> Unit = {},
    onCalendarButtonClick : () -> Unit = {},
    month: String = "",
    day : String = ""
){
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement
            .spacedBy(
                8.dp,
                Alignment.CenterVertically
            )
    ) {
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberButton(modifier = Modifier.weight(1f), text = "7", onClick = onItemClick)
            NumberButton(modifier = Modifier.weight(1f), text = "8", onClick = onItemClick)
            NumberButton(modifier = Modifier.weight(1f), text = "9", onClick = onItemClick)
            NumberButton(modifier = Modifier.weight(1f), text = "<", onClick = { onDelete()})
        }
        Row (
            modifier = Modifier.fillMaxWidth()
        ){
            Column(
                modifier = Modifier.weight(3f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ){
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NumberButton(modifier = Modifier.weight(1f), text = "4", onClick = onItemClick)
                    NumberButton(modifier = Modifier.weight(1f), text = "5", onClick = onItemClick)
                    NumberButton(modifier = Modifier.weight(1f), text = "6", onClick = onItemClick)
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    NumberButton(modifier = Modifier.weight(1f), text = "1", onClick = onItemClick)
                    NumberButton(modifier = Modifier.weight(1f), text = "2", onClick = onItemClick)
                    NumberButton(modifier = Modifier.weight(1f), text = "3", onClick = onItemClick)
                }
            }
            CalendarButton(
                modifier = Modifier
                    .weight(1f)
                    .padding(8.dp)
                ,
                onClick = onCalendarButtonClick,
                month = month.toInt(),
                day = day.toInt()
            )

        }
        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            NumberButton(modifier = Modifier.weight(1f), text = "0", onClick = onItemClick)
            NumberButton(modifier = Modifier.weight(1f), text = "00", onClick = onItemClick)
            NumberButton(modifier = Modifier.weight(1f), text = "000", onClick = onItemClick)
            NumberButton(
                modifier = Modifier.weight(1f),
                text = "OK", onClick = {onOkClick()} ,
                textColor =  MaterialTheme.colorScheme.tertiary
            )
        }

    }
}

@Composable
private fun CalendarButton(
    modifier : Modifier = Modifier,
    month: Int = 1,
    day: Int = 6,
    onClick: () -> Unit = {},
    textColor : Color = MaterialTheme.colorScheme.onSurface
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(144.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(
                color = MaterialTheme.colorScheme.background,
                shape = RoundedCornerShape(16.dp)
            )
            .noRippleClick { onClick() }
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground,
                shape = RoundedCornerShape(16.dp)
            )
        ,
        contentAlignment = Alignment.Center
    ){
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Icon(
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(4.dp))
            Texts.TitleMedium(
                text = "$month/$day",
                textAlign = TextAlign.Center,
                color = textColor,
            )
        }
    }

}


@Composable
private fun NumberButton(
    modifier: Modifier = Modifier,
    text: String,
    onClick: (String) -> Unit = {},
    textColor: Color = MaterialTheme.colorScheme.onSurface
){
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center){
        Box(
            modifier = Modifier
                .size(72.dp)
                .clip(CircleShape)
                .background(
                    color = MaterialTheme.colorScheme.background,
                    shape = CircleShape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onBackground,
                    shape = CircleShape
                )
                .noRippleClick { onClick(text) }
            ,
            contentAlignment = Alignment.Center,
        ){
            Texts.TitleMedium(
                text = text,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                color = textColor,
                fonSize = 24.sp
            )

        }

    }
}