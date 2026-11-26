package core.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.domain.util.zeroStringDisplay
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.cd_next_month
import moneymanagerkmp.composeapp.generated.resources.cd_previous_month
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

/**
 * D3 MonthPill date selector.
 */
@Composable
fun DatePicker(
    year: Int,
    month: Int,
    onDateChange: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    val dateString = remember(year, month) {
        "${year}-${zeroStringDisplay(month)}"
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = MoneyTokens.ScreenHorizontalPadding, vertical = 4.dp),
        contentAlignment = Alignment.Center,
    ) {
        Surface(
            shape = CircleShape,
            color = MoneyTheme.colors.surface,
            contentColor = MoneyTheme.colors.ink,
            border = BorderStroke(1.dp, MoneyTheme.colors.cardBorder),
            shadowElevation = MoneyTokens.CardElevation,
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                IconButton(
                    modifier = Modifier.size(36.dp),
                    onClick = {
                        if (month - 1 <= 0) {
                            onDateChange(year - 1, 12)
                        } else {
                            onDateChange(year, month - 1)
                        }
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                        contentDescription = stringResource(Res.string.cd_previous_month),
                        tint = MoneyTheme.colors.muted,
                    )
                }

                Text(
                    modifier = Modifier.padding(horizontal = 10.dp),
                    text = dateString,
                    style = MaterialTheme.typography.titleSmall.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                    color = MoneyTheme.colors.ink,
                )

                IconButton(
                    modifier = Modifier.size(36.dp),
                    onClick = {
                        if (month + 1 >= 13) {
                            onDateChange(year + 1, 1)
                        } else {
                            onDateChange(year, month + 1)
                        }
                    },
                ) {
                    Icon(
                        modifier = Modifier.size(20.dp),
                        imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                        contentDescription = stringResource(Res.string.cd_next_month),
                        tint = MoneyTheme.colors.muted,
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun DatePickerLightPreview() {
    AppTheme(darkTheme = false) {
        DatePicker(year = 2026, month = 11, onDateChange = { _, _ -> })
    }
}

@Preview
@Composable
private fun DatePickerDarkPreview() {
    AppTheme(darkTheme = true) {
        DatePicker(year = 2026, month = 11, onDateChange = { _, _ -> })
    }
}
