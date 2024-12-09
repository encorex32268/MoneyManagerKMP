@file:OptIn(ExperimentalResourceApi::class)

package feature.home.presentation.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
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
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import feature.core.presentation.Texts
import feature.core.ui.light_ErrorColorContainer
import feature.home.presentation.add.AddState
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_done
import moneymanagerkmp.composeapp.generated.resources.baseline_remove
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

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
    day : String = "",
    aspectRatio: Float = 1.25f
){
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
                modifier = Modifier.aspectRatio(aspectRatio), text = "7", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "4", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "1", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "0", onClick = onItemClick
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "8", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "5", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "2", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "00", onClick = onItemClick
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "9", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "6", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "3", onClick = onItemClick
            )
            NumberButton(
                modifier = Modifier.aspectRatio(aspectRatio), text = "000", onClick = onItemClick
            )
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CalculateIconButton(
                modifier = Modifier.aspectRatio(aspectRatio),
                icon = vectorResource(Res.drawable.baseline_remove),
                contentDescription = "remove button",
                onClick = onDelete,
                backgroundColor = light_ErrorColorContainer
            )
            CalendarButton(
                modifier = Modifier.aspectRatio(aspectRatio / 2),
                month = month.toInt(),
                day = day.toInt(),
                onClick = onCalendarButtonClick,

            )
            CalculateIconButton(
                modifier = Modifier.aspectRatio(aspectRatio),
                icon = vectorResource(Res.drawable.baseline_done),
                contentDescription = "done button",
                onClick = onOkClick,
                backgroundColor = Color(69, 230, 0, 59)
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
    tint: Color = MaterialTheme.colorScheme.onBackground,
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
            contentDescription = contentDescription,
            tint = tint
        )

    }
}




@Composable
private fun CalendarButton(
    modifier : Modifier = Modifier,
    month: Int = 1,
    day: Int = 6,
    onClick: () -> Unit = {},
    shape: Shape = RoundedCornerShape(16.dp)
){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .size(36.dp)
            .clip(shape)
            .background(
                color = Color(227,236,254),
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
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onSurface,
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$month/$day",
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
    onClick: (String) -> Unit = {},
    shape: Shape = RoundedCornerShape(24.dp)
){
    Box(
        modifier = modifier
            .clip(shape)
            .clickable { onClick(text) }
            .background(
                color = MaterialTheme.colorScheme.background,
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