@file:OptIn(ExperimentalResourceApi::class)

package feature.core.presentation.components

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import feature.core.presentation.Texts
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import moneymanagerkmp.composeapp.generated.resources.notosanslao_bold
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
               Texts.BodyMedium(
                   text = stringResource(Res.string.dialog_ok_button),
                   font = Res.font.notosanslao_bold
               )
           }
        },
        title = {
            Texts.TitleMedium(
                text = title
            )
        },
        text = {
            Texts.BodyMedium(
                text = content
            )
        }
    )

}
