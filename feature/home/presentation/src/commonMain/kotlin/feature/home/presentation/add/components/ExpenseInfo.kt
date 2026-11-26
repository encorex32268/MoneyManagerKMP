package feature.home.presentation.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.presentation.CategoryList
import core.presentation.model.CategoryUi
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.description
import org.jetbrains.compose.resources.stringResource
import toMoneyString

/**
 * D3 Numpad Header:
 * Left: Pure white pill matching digit keys with 38dp pastel icon circle + 24dp icon + editable name.
 * Right: Muted '$' prefix + 28sp ExtraBold tabular amount.
 */
@Composable
fun ExpenseInfo(
    modifier: Modifier = Modifier,
    categoryUi: CategoryUi?,
    description: String,
    cost: Long,
    onValueChange: (String) -> Unit,
) {
    val groupColor = categoryUi?.colorArgb?.let { Color(it) }
        ?: CategoryList.getColorByTypeId(categoryUi?.typeId ?: 0L)
    val iconPainter = CategoryList.getCategoryIconById(categoryUi?.id?.toLong() ?: 0L)
    val formattedAmount = cost.toMoneyString().removePrefix("$")

    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Surface(
            modifier = Modifier.weight(1f, fill = false),
            shape = CircleShape,
            color = MoneyTheme.colors.numpadKey,
            shadowElevation = 1.dp,
        ) {
            Row(
                modifier = Modifier.padding(start = 4.dp, end = 16.dp, top = 4.dp, bottom = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                Box(
                    modifier = Modifier
                        .size(MoneyTokens.NumpadPillIconBox)
                        .background(
                            color = groupColor.copy(alpha = 0.28f),
                            shape = CircleShape,
                        ),
                    contentAlignment = Alignment.Center,
                ) {
                    Icon(
                        modifier = Modifier.size(MoneyTokens.NumpadPillIcon),
                        painter = iconPainter,
                        contentDescription = null,
                        tint = MoneyTheme.colors.ink,
                    )
                }

                BasicTextField(
                    modifier = Modifier.widthIn(min = 56.dp, max = 148.dp),
                    value = description,
                    onValueChange = onValueChange,
                    maxLines = 1,
                    singleLine = true,
                    decorationBox = { innerTextField ->
                        if (description.isEmpty()) {
                            Text(
                                text = stringResource(Res.string.description),
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontSize = 15.5.sp,
                                    fontWeight = FontWeight.Bold,
                                ),
                                color = MoneyTheme.colors.muted,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        innerTextField()
                    },
                    textStyle = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 15.5.sp,
                        fontWeight = FontWeight.Bold,
                        color = MoneyTheme.colors.ink,
                    ),
                    cursorBrush = SolidColor(MoneyTheme.colors.ink),
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Row(
            verticalAlignment = Alignment.Bottom,
            horizontalArrangement = Arrangement.spacedBy(2.dp),
        ) {
            Text(
                modifier = Modifier.padding(bottom = 3.dp),
                text = "$",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold,
                ),
                color = MoneyTheme.colors.muted,
            )
            Text(
                text = formattedAmount,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.headlineMedium,
                color = MoneyTheme.colors.ink,
            )
        }
    }
}
