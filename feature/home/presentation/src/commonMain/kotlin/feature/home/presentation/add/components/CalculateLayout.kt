@file:OptIn(ExperimentalResourceApi::class)

package feature.home.presentation.add.components

import androidx.compose.foundation.background
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.navigation.NavigationLayoutType
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.home.presentation.add.AddAction
import feature.home.presentation.add.AddState
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_done
import moneymanagerkmp.composeapp.generated.resources.baseline_remove
import moneymanagerkmp.composeapp.generated.resources.cd_delete
import moneymanagerkmp.composeapp.generated.resources.cd_save
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import toDateString

private val KeyGap = 9.dp
private val KeyShape = RoundedCornerShape(MoneyTokens.KeyRadius)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalculateLayout(
    modifier: Modifier = Modifier,
    onAction: (AddAction) -> Unit = {},
    month: String = "",
    day: String = "",
    state: AddState,
    numberButtonAspectRatio: Float = 1.25f,
    navigationLayoutType: NavigationLayoutType = NavigationLayoutType.BOTTOM_NAVIGATION,
) {
    val datePickerState = rememberDatePickerState(
        initialDisplayMode = when (navigationLayoutType) {
            NavigationLayoutType.BOTTOM_NAVIGATION -> DisplayMode.Picker
            else -> DisplayMode.Input
        }
    )
    val isShowDialog = rememberSaveable { mutableStateOf(false) }

    if (isShowDialog.value) {
        DatePickerDialog(
            onDismissRequest = { isShowDialog.value = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        onAction(
                            AddAction.OnSelectedDate(
                                timestamp = datePickerState.selectedDateMillis ?: 0L
                            )
                        )
                        isShowDialog.value = false
                    }
                ) {
                    Text(
                        text = stringResource(Res.string.dialog_ok_button),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            },
            dismissButton = {
                TextButton(onClick = { isShowDialog.value = false }) {
                    Text(
                        text = stringResource(Res.string.dialog_cancel_button),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            },
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ExpenseInfo(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 14.dp),
            onValueChange = {
                onAction(AddAction.OnDescriptionChange(it))
            },
            categoryUi = state.categoryUi,
            description = state.description,
            cost = state.cost.toLongOrNull() ?: 0L,
        )
        CalculateKeyboard(
            onCalendarButtonClick = { isShowDialog.value = true },
            onAction = onAction,
            month = month,
            day = day,
        )
    }
}

@Composable
private fun CalculateKeyboard(
    onCalendarButtonClick: () -> Unit = {},
    month: String = "",
    day: String = "",
    onAction: (AddAction) -> Unit = {},
) {
    val colors = MoneyTheme.colors
    val singleKeyModifier = Modifier
        .fillMaxWidth()
        .height(MoneyTokens.NumpadKeyHeight)
    val doubleKeyHeight = MoneyTokens.NumpadKeyHeight * 2 + KeyGap

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Bottom,
        horizontalArrangement = Arrangement.spacedBy(KeyGap),
    ) {
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(KeyGap),
        ) {
            NumberButton(modifier = singleKeyModifier, text = "7", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "4", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "1", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "0", onAction = onAction)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(KeyGap),
        ) {
            NumberButton(modifier = singleKeyModifier, text = "8", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "5", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "2", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "00", onAction = onAction)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(KeyGap),
        ) {
            NumberButton(modifier = singleKeyModifier, text = "9", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "6", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "3", onAction = onAction)
            NumberButton(modifier = singleKeyModifier, text = "000", onAction = onAction)
        }

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(KeyGap),
        ) {
            CalculateIconButton(
                modifier = singleKeyModifier,
                icon = vectorResource(Res.drawable.baseline_remove),
                contentDescription = stringResource(Res.string.cd_delete),
                onClick = { onAction(AddAction.OnDeleteTextClick) },
                backgroundColor = colors.numpadDelete,
                contentColor = colors.numpadDeleteFg,
            )
            CalendarButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(doubleKeyHeight),
                month = month.toIntOrNull() ?: 1,
                day = day.toIntOrNull() ?: 1,
                onClick = onCalendarButtonClick,
                backgroundColor = colors.numpadDate,
                contentColor = colors.numpadDateFg,
            )
            CalculateIconButton(
                modifier = singleKeyModifier,
                icon = vectorResource(Res.drawable.baseline_done),
                contentDescription = stringResource(Res.string.cd_save),
                onClick = { onAction(AddAction.OnSaveClick) },
                backgroundColor = colors.numpadDone,
                contentColor = colors.numpadDoneFg,
            )
        }
    }
}

@Composable
private fun CalculateIconButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    backgroundColor: Color,
    contentColor: Color,
    shape: Shape = KeyShape,
    icon: ImageVector,
    contentDescription: String? = null,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color = backgroundColor, shape = shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Icon(
            modifier = Modifier.size(22.dp),
            imageVector = icon,
            contentDescription = contentDescription,
            tint = contentColor,
        )
    }
}

@Composable
private fun CalendarButton(
    modifier: Modifier = Modifier,
    month: Int = 1,
    day: Int = 1,
    onClick: () -> Unit = {},
    shape: Shape = KeyShape,
    backgroundColor: Color,
    contentColor: Color,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color = backgroundColor, shape = shape)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center,
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                modifier = Modifier.size(18.dp),
                imageVector = Icons.Outlined.DateRange,
                contentDescription = null,
                tint = contentColor,
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = toDateString(month, day),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium.copy(
                    fontSize = 12.5.sp,
                    fontWeight = FontWeight.Bold,
                ),
                color = contentColor,
            )
        }
    }
}

@Composable
private fun NumberButton(
    modifier: Modifier = Modifier,
    text: String,
    onAction: (AddAction) -> Unit = {},
    shape: Shape = KeyShape,
) {
    Box(
        modifier = modifier
            .clip(shape)
            .background(color = MoneyTheme.colors.numpadKey, shape = shape)
            .clickable { onAction(AddAction.OnCostChange(text)) },
        contentAlignment = Alignment.Center,
    ) {
        Text(
            text = text,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 19.sp,
                fontWeight = FontWeight.SemiBold,
            ),
            color = MoneyTheme.colors.numpadKeyFg,
        )
    }
}
