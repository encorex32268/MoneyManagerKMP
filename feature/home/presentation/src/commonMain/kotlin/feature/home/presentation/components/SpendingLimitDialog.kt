package feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import app.presentation.AppTheme
import core.presentation.components.BudgetWarningBar
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.budget_warn_high_label
import moneymanagerkmp.composeapp.generated.resources.budget_warn_mid_label
import moneymanagerkmp.composeapp.generated.resources.budget_warning_thresholds
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.home_spending_limit_set
import moneymanagerkmp.composeapp.generated.resources.home_spending_limit_set_hint
import moneymanagerkmp.composeapp.generated.resources.save
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toMoneyString
import kotlin.math.roundToInt

@Composable
fun SpendingLimitDialog(
    currentLimit: Long = 0L,
    currentWarnMid: Int = MoneyTokens.DefaultWarnMidPercent,
    currentWarnHigh: Int = MoneyTokens.DefaultWarnHighPercent,
    onDismissRequest: () -> Unit = {},
    onConfirmButtonClick: (limitText: String, warnMidPercent: Int, warnHighPercent: Int) -> Unit = { _, _, _ -> },
) {
    var limitText by remember(currentLimit) {
        mutableStateOf(if (currentLimit > 0L) currentLimit.toString() else "")
    }
    var warnMid by remember(currentWarnMid) {
        mutableIntStateOf(currentWarnMid.coerceIn(10, 85))
    }
    var warnHigh by remember(currentWarnHigh) {
        mutableIntStateOf(currentWarnHigh.coerceIn((warnMid + 5).coerceAtMost(95), 95))
    }
    val parsedLimit = limitText.toLongOrNull() ?: 0L

    Dialog(onDismissRequest = onDismissRequest) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(MoneyTokens.ModalRadius),
            color = MoneyTheme.colors.surface,
            shadowElevation = 12.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
            ) {
                Text(
                    text = stringResource(Res.string.home_spending_limit_set),
                    style = MaterialTheme.typography.titleMedium,
                    color = MoneyTheme.colors.ink,
                )

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .background(
                            color = MoneyTheme.colors.bg,
                            shape = RoundedCornerShape(14.dp),
                        )
                        .border(
                            width = 1.5.dp,
                            color = MoneyTheme.colors.line,
                            shape = RoundedCornerShape(14.dp),
                        )
                        .padding(horizontal = 14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "$",
                        style = MaterialTheme.typography.titleMedium,
                        color = MoneyTheme.colors.muted,
                    )
                    BasicTextField(
                        modifier = Modifier.weight(1f),
                        value = limitText,
                        onValueChange = { input ->
                            limitText = input.filter { it.isDigit() }
                        },
                        maxLines = 1,
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            Box(contentAlignment = Alignment.CenterStart) {
                                if (limitText.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.home_spending_limit_set_hint),
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = MoneyTheme.colors.muted,
                                    )
                                }
                                innerTextField()
                            }
                        },
                        textStyle = MaterialTheme.typography.titleLarge.copy(
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = MoneyTheme.colors.ink,
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.NumberPassword,
                        ),
                        cursorBrush = SolidColor(MoneyTheme.colors.ink),
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = stringResource(Res.string.budget_warning_thresholds),
                    style = MaterialTheme.typography.labelMedium,
                    color = MoneyTheme.colors.muted,
                )
                Spacer(modifier = Modifier.height(8.dp))
                BudgetWarningBar(
                    warnMidPercent = warnMid,
                    warnHighPercent = warnHigh,
                )

                Spacer(modifier = Modifier.height(14.dp))

                ThresholdSliderRow(
                    label = stringResource(Res.string.budget_warn_mid_label),
                    percent = warnMid,
                    amountPreview = ((parsedLimit * warnMid) / 100L).toMoneyString(),
                    accentColor = MoneyTheme.colors.budgetWarn,
                    valueRange = 10f..85f,
                    onValueChange = { raw ->
                        val snapped = ((raw / 5f).roundToInt() * 5).coerceIn(10, 85)
                        warnMid = snapped
                        if (warnMid >= warnHigh) {
                            warnHigh = (warnMid + 5).coerceAtMost(95)
                        }
                    },
                )

                Spacer(modifier = Modifier.height(6.dp))

                ThresholdSliderRow(
                    label = stringResource(Res.string.budget_warn_high_label),
                    percent = warnHigh,
                    amountPreview = ((parsedLimit * warnHigh) / 100L).toMoneyString(),
                    accentColor = MoneyTheme.colors.budgetDanger,
                    valueRange = 20f..95f,
                    onValueChange = { raw ->
                        val snapped = ((raw / 5f).roundToInt() * 5).coerceIn(20, 95)
                        warnHigh = snapped
                        if (warnHigh <= warnMid) {
                            warnMid = (warnHigh - 5).coerceAtLeast(10)
                        }
                    },
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    OutlinedButton(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        border = BorderStroke(1.dp, MoneyTheme.colors.line),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = MoneyTheme.colors.ink,
                        ),
                        onClick = onDismissRequest,
                    ) {
                        Text(
                            text = stringResource(Res.string.dialog_cancel_button),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                    Button(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp),
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MoneyTheme.colors.accent,
                            contentColor = MoneyTheme.colors.accentFg,
                        ),
                        onClick = {
                            onConfirmButtonClick(limitText, warnMid, warnHigh)
                            onDismissRequest()
                        },
                    ) {
                        Text(
                            text = stringResource(Res.string.save),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ThresholdSliderRow(
    label: String,
    percent: Int,
    amountPreview: String,
    accentColor: Color,
    valueRange: ClosedFloatingPointRange<Float>,
    onValueChange: (Float) -> Unit,
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(9.dp)
                        .background(color = accentColor, shape = CircleShape)
                )
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelMedium,
                    color = MoneyTheme.colors.muted,
                )
            }
            Text(
                text = "$percent% · $amountPreview",
                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                color = MoneyTheme.colors.ink,
            )
        }
        Slider(
            value = percent.toFloat(),
            onValueChange = onValueChange,
            valueRange = valueRange,
            steps = 14,
            colors = SliderDefaults.colors(
                thumbColor = accentColor,
                activeTrackColor = accentColor,
                inactiveTrackColor = MoneyTheme.colors.surfaceAlt,
                activeTickColor = Color.Transparent,
                inactiveTickColor = Color.Transparent,
            ),
        )
    }
}

@Preview
@Composable
private fun SpendingLimitDialogLightPreview() {
    AppTheme(darkTheme = false) {
        SpendingLimitDialog(currentLimit = 50000L)
    }
}

@Preview
@Composable
private fun SpendingLimitDialogDarkPreview() {
    AppTheme(darkTheme = true) {
        SpendingLimitDialog(currentLimit = 50000L, currentWarnMid = 40, currentWarnHigh = 80)
    }
}
