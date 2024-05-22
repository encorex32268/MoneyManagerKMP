@file:OptIn(ExperimentalResourceApi::class)

package feature.core.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun TwoButtonDialog(
    title : String,
    content : String,
    titleTextStyle : TextStyle = MaterialTheme.typography.titleMedium,
    contentTextStyle: TextStyle= MaterialTheme.typography.bodyMedium,
    onDismissRequest : () -> Unit = {},
    onConfirmButtonClick : () -> Unit = {},
) {

    AlertDialog(
        onDismissRequest = onDismissRequest,
        confirmButton = {
           TextButton(onClick = {
               onConfirmButtonClick()
               onDismissRequest()
           }) {
               Text(text = stringResource(Res.string.dialog_ok_button))
           }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismissRequest()
            }) {
                Text(text = stringResource(Res.string.dialog_cancel_button))
            }
        },
        title = {
            Text(text = title , style = titleTextStyle)
        },
        text = {
            Text(text = content , style = contentTextStyle)
        }
    )

}
