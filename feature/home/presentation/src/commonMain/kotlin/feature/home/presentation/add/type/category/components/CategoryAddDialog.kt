package feature.home.presentation.add.type.category.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import core.presentation.CategoryList
import core.presentation.components.CircleIcon
import core.presentation.model.CategoryUi
import core.presentation.noRippleClick
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.category_add_description
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import org.jetbrains.compose.resources.stringResource

@Composable
fun CategoryAddDialog(
    categoryUi: CategoryUi,
    groupColorArgb: Int? = null,
    onDismissRequest: () -> Unit = {},
    onDone: (CategoryUi) -> Unit = {},
) {
    var currentCategoryUi by remember { mutableStateOf(categoryUi) }
    val badgeColor = groupColorArgb?.let { Color(it) }
        ?: categoryUi.colorArgb?.let { Color(it) }
        ?: MoneyTokens.CatFood

    Dialog(onDismissRequest = onDismissRequest) {
        val keyboard = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClick {
                    keyboard?.hide()
                    focusManager.clearFocus(true)
                },
            shape = RoundedCornerShape(MoneyTokens.ModalRadius),
            color = MoneyTheme.colors.surface,
            shadowElevation = 12.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(22.dp),
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                ) {
                    CircleIcon(
                        modifier = Modifier.size(42.dp),
                        isClicked = true,
                        image = CategoryList.getCategoryIconById(categoryUi.id.toLong()),
                        backgroundColor = badgeColor,
                    )
                    Text(
                        text = stringResource(Res.string.category_add_description),
                        style = MaterialTheme.typography.titleMedium,
                        color = MoneyTheme.colors.ink,
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .background(
                            color = MoneyTheme.colors.bg,
                            shape = RoundedCornerShape(13.dp),
                        )
                        .border(
                            width = 1.dp,
                            color = MoneyTheme.colors.line,
                            shape = RoundedCornerShape(13.dp),
                        )
                        .padding(horizontal = 14.dp),
                    contentAlignment = Alignment.CenterStart,
                ) {
                    BasicTextField(
                        modifier = Modifier.fillMaxWidth(),
                        value = currentCategoryUi.name,
                        onValueChange = {
                            currentCategoryUi = currentCategoryUi.copy(name = it)
                        },
                        maxLines = 1,
                        singleLine = true,
                        decorationBox = { innerTextField ->
                            if (currentCategoryUi.name.isEmpty()) {
                                Text(
                                    text = stringResource(Res.string.category_add_description),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MoneyTheme.colors.muted,
                                )
                            }
                            innerTextField()
                        },
                        textStyle = MaterialTheme.typography.bodyLarge.copy(
                            color = MoneyTheme.colors.ink,
                        ),
                        cursorBrush = SolidColor(MoneyTheme.colors.ink),
                    )
                }

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
                            onDone(currentCategoryUi)
                            onDismissRequest()
                        },
                    ) {
                        Text(
                            text = stringResource(Res.string.dialog_ok_button),
                            style = MaterialTheme.typography.labelLarge,
                        )
                    }
                }
            }
        }
    }
}
