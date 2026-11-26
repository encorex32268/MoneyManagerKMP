@file:OptIn(ExperimentalResourceApi::class)

package core.presentation.components

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_content
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_title
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun DeleteDialog(
    title: String = stringResource(Res.string.dialog_delete_title),
    content: String = stringResource(Res.string.dialog_delete_content),
    onDismissRequest: () -> Unit = {},
    onConfirmButtonClick: () -> Unit = {},
    confirmTextColor: Color = MoneyTheme.colors.budgetDanger,
    dismissTextColor: Color = MoneyTheme.colors.muted,
    dismissText: String = stringResource(Res.string.dialog_cancel_button),
    confirmText: String = stringResource(Res.string.dialog_delete_title),
) {
    AlertDialog(
        onDismissRequest = onDismissRequest,
        shape = RoundedCornerShape(MoneyTokens.ModalRadius),
        containerColor = MoneyTheme.colors.surface,
        titleContentColor = MoneyTheme.colors.ink,
        textContentColor = MoneyTheme.colors.muted,
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmButtonClick()
                    onDismissRequest()
                },
            ) {
                Text(
                    text = confirmText,
                    style = MaterialTheme.typography.labelLarge,
                    color = confirmTextColor,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest) {
                Text(
                    text = dismissText,
                    style = MaterialTheme.typography.labelLarge,
                    color = dismissTextColor,
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium,
            )
        },
        text = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium,
            )
        },
    )
}
