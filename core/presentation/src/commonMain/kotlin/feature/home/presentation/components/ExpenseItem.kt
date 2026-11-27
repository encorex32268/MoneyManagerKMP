package feature.home.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import app.presentation.AppTheme
import core.presentation.CategoryList
import core.presentation.components.CircleIcon
import core.presentation.components.MoneyCard
import core.presentation.date.toDayOfWeekStringResource
import core.presentation.date.toLocalDateTime
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import feature.home.presentation.model.ExpenseUi
import org.jetbrains.compose.resources.stringResource
import androidx.compose.ui.tooling.preview.Preview
import toDateString
import toMoneyString

/**
 * D3 Day Card grouping expenses for a single date.
 */
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
        "$dateString ($dayOfWeekString)"
    }

    MoneyCard(
        modifier = modifier,
        contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 14.dp, bottom = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.weight(1f),
                text = date,
                style = MaterialTheme.typography.labelMedium,
                color = MoneyTheme.colors.muted,
            )
            Text(
                text = total.toMoneyString(),
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                ),
                color = MoneyTheme.colors.ink,
            )
        }

        HorizontalDivider(
            thickness = 1.dp,
            color = MoneyTheme.colors.line,
        )

        Spacer(modifier = Modifier.height(4.dp))

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
            .clip(RoundedCornerShape(10.dp))
            .then(
                if (isClick) {
                    Modifier.clickable(onClick = onClick)
                } else {
                    Modifier
                }
            )
            .padding(vertical = 10.dp, horizontal = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CircleIcon(
            modifier = Modifier.size(MoneyTokens.BadgeIconSize),
            isClicked = true,
            image = CategoryList.getCategoryIconById(expense.categoryId.toLong()),
            backgroundColor = backgroundColor,
        )
        Spacer(modifier = Modifier.width(12.dp))
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                text = expense.description,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MoneyTheme.colors.ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            if (expense.content.trim().isNotEmpty()) {
                Text(
                    text = expense.content.trim(),
                    style = MaterialTheme.typography.labelSmall.copy(
                        fontWeight = FontWeight.Normal,
                    ),
                    color = MoneyTheme.colors.muted,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = expense.cost.toMoneyString(),
            style = MaterialTheme.typography.bodyMedium.copy(
                fontSize = 14.5.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.End,
            ),
            color = MoneyTheme.colors.ink,
        )
    }
}

private val previewExpenses = listOf(
    ExpenseUi(
        id = "1",
        categoryId = 1,
        typeId = 1L,
        description = "速食",
        cost = 500,
        timestamp = 1751155200000L,
        content = "週末跟朋友聚餐吃漢堡薯條",
    ),
    ExpenseUi(
        id = "2",
        categoryId = 2,
        typeId = 2L,
        description = "飲料",
        cost = 65,
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
