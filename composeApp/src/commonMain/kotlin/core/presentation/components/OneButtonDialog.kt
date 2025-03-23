@file:OptIn(ExperimentalResourceApi::class)

package core.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun OneButtonDialog(
    title : String,
    content : String,
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
               Text(
                   text = stringResource(Res.string.dialog_ok_button),
                   style = MaterialTheme.typography.labelLarge.copy(
                       fontSize = 14.sp
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
