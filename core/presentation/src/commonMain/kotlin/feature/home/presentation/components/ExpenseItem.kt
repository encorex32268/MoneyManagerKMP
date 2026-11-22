package feature.home.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import app.presentation.AppTheme
import core.presentation.CategoryList
import core.presentation.components.CircleIcon
import core.presentation.components.ReceiptShape
import core.presentation.date.toDayOfWeekStringResource
import core.presentation.date.toLocalDateTime
import feature.home.presentation.model.ExpenseUi
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_sticky_note_24
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import toDateString
import toMoneyString

private val ReceiptCardShape = ReceiptShape(
    cornerRadius = 16.dp,
    toothWidth = 10.dp,
    toothHeight = 5.dp,
)

@Composable
fun ExpenseItem(
    modifier: Modifier = Modifier,
    items: List<ExpenseUi>,
    onItemClick: (ExpenseUi) -> Unit = {},
    isClick: Boolean = true,
) {
    if (items.isEmpty()) return

    val total = remember(items) {
        items.sumOf { it.cost }
    }
    val localDateTime = remember(items) {
        (items.firstOrNull()?.timestamp ?: 0L).toLocalDateTime()
    }
    val dayOfWeekString = stringResource(localDateTime.toDayOfWeekStringResource())
    val date = remember(localDateTime, dayOfWeekString) {
        val dateString = toDateString(localDateTime.monthNumber, localDateTime.dayOfMonth)
        "$dateString $dayOfWeekString"
    }

    Surface(
        modifier = modifier,
        shape = ReceiptCardShape,
        color = MaterialTheme.colorScheme.surfaceContainerLowest,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant),
        shadowElevation = 1.dp,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = date,
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontWeight = FontWeight.SemiBold,
                    ),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                )
                Text(
                    text = total.toMoneyString(),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.End,
                    ),
                    color = MaterialTheme.colorScheme.onSurface,
                )
            }

            DashedReceiptDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
            )

            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(2.dp),
            ) {
                items.forEach { expense ->
                    ExpenseRow(
                        expense = expense,
                        isClick = isClick,
                        onClick = { onItemClick(expense) },
                    )
                }
            }
        }
    }
}

@Composable
private fun DashedReceiptDivider(
    modifier: Modifier = Modifier,
) {
    val dividerColor = MaterialTheme.colorScheme.outlineVariant
    Canvas(
        modifier = modifier.height(1.dp),
    ) {
        drawLine(
            color = dividerColor,
            start = Offset(0f, size.height / 2f),
            end = Offset(size.width, size.height / 2f),
            strokeWidth = 1.dp.toPx(),
            pathEffect = PathEffect.dashPathEffect(
                intervals = floatArrayOf(12f, 8f),
                phase = 0f,
            ),
        )
    }
}

@Composable
private fun ExpenseRow(
    expense: ExpenseUi,
    isClick: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val backgroundColor = remember(expense.type, expense.typeId) {
        if (expense.type == null) {
            CategoryList.getColorByTypeId(expense.typeId)
        } else {
            Color(expense.type.colorArgb)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .then(
                if (isClick) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(vertical = 6.dp, horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIcon(
            modifier = Modifier.size(34.dp),
            isClicked = isClick,
            image = CategoryList.getCategoryIconById(expense.categoryId.toLong()),
            backgroundColor = backgroundColor,
        )
        Spacer(modifier = Modifier.width(10.dp))
        Row(
            modifier = Modifier.weight(1f),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = expense.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (expense.content.trim().isNotEmpty()) {
                Spacer(modifier = Modifier.width(4.dp))
                Icon(
                    modifier = Modifier.size(14.dp),
                    imageVector = vectorResource(Res.drawable.baseline_sticky_note_24),
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = expense.cost.toMoneyString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
            ),
            color = MaterialTheme.colorScheme.onSurface,
        )
    }
}

private val previewExpenses = listOf(
    ExpenseUi(
        id = "1",
        categoryId = 1,
        typeId = 1L,
        description = "襯衫",
        cost = 150,
        timestamp = 1751155200000L,
        content = "Summer sale",
    ),
    ExpenseUi(
        id = "2",
        categoryId = 2,
        typeId = 2L,
        description = "運動",
        cost = 900,
        timestamp = 1751155200000L,
    ),
    ExpenseUi(
        id = "3",
        categoryId = 3,
        typeId = 3L,
        description = "火車",
        cost = 60,
        timestamp = 1751155200000L,
    ),
)

@Preview
@Composable
private fun ExpenseItemDefaultPreview() {
    AppTheme(darkTheme = false) {
        ExpenseItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            items = previewExpenses,
        )
    }
}

@Preview
@Composable
private fun ExpenseItemDarkPreview() {
    AppTheme(darkTheme = true) {
        ExpenseItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            items = previewExpenses,
        )
    }
}

@Preview
@Composable
private fun ExpenseItemSinglePreview() {
    AppTheme(darkTheme = false) {
        ExpenseItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            items = listOf(previewExpenses.first()),
        )
    }
}

@Preview
@Composable
private fun ExpenseItemReadOnlyPreview() {
    AppTheme(darkTheme = false) {
        ExpenseItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            items = previewExpenses,
            isClick = false,
        )
    }
}

@Preview
@Composable
private fun ExpenseItemLongTextPreview() {
    AppTheme(darkTheme = false) {
        ExpenseItem(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            items = listOf(
                ExpenseUi(
                    id = "1",
                    categoryId = 1,
                    typeId = 1L,
                    description = "Supermarket weekly grocery shopping with snacks and drinks",
                    cost = 12345,
                    timestamp = 1751155200000L,
                    content = "Note",
                ),
            ),
        )
    }
}

@Preview
@Composable
private fun ExpenseItemLargeFontPreview() {
    val density = LocalDensity.current
    CompositionLocalProvider(
        LocalDensity provides Density(density = density.density, fontScale = 2f)
    ) {
        AppTheme(darkTheme = false) {
            ExpenseItem(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                items = previewExpenses,
            )
        }
    }
}
