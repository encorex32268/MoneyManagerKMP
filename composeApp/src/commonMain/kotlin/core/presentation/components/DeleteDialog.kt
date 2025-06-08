@file:OptIn(ExperimentalResourceApi::class)

package core.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_content
import moneymanagerkmp.composeapp.generated.resources.dialog_delete_title
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource


@Composable
fun DeleteDialog(
    title : String = stringResource(Res.string.dialog_delete_title),,
    content : String = stringResource(Res.string.dialog_delete_content),
    onDismissRequest : () -> Unit = {},
    onConfirmButtonClick : () -> Unit = {},
    confirmTextColor: Color = MaterialTheme.colorScheme.error,
    dismissTextColor: Color = MaterialTheme.colorScheme.onBackground,
    dismissText: String = stringResource(Res.string.dialog_cancel_button),
    confirmText: String = stringResource(Res.string.dialog_delete_title),
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
           TextButton(onClick = {
               onConfirmButtonClick()
               onDismissRequest()
           }) {
               Text(
                   text = confirmText,
                   style = MaterialTheme.typography.labelLarge.copy(
                       fontSize = 14.sp,
                       color = confirmTextColor
                   )
               )
           }
        },
        dismissButton = {
            TextButton(onClick = onDismissRequest ) {
                Text(
                    text = dismissText,
                    style = MaterialTheme.typography.labelLarge.copy(
                        fontSize = 14.sp,
                        color = dismissTextColor.copy(
                            alpha = 0.5f
                        )
                    )
                )
            }
        },
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.labelLarge
            )
        },
        text = {
            Text(
                text = content,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    )

}
