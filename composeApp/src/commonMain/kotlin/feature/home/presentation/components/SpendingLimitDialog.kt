package feature.home.presentation.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.dialog_ok_button
import moneymanagerkmp.composeapp.generated.resources.home_spending_limit_set
import moneymanagerkmp.composeapp.generated.resources.home_spending_limit_set_hint
import org.jetbrains.compose.resources.stringResource

@Composable
fun SpendingLimitDialog(
    onDismissRequest : () -> Unit = {},
    onConfirmButtonClick : (String) -> Unit = {},
) {
    var limitText by remember {
        mutableStateOf("")
    }

    AlertDialog(
        modifier = Modifier.fillMaxWidth(),
        onDismissRequest = onDismissRequest,
        confirmButton = {
            TextButton(onClick = {
                onConfirmButtonClick(limitText)
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
                text = stringResource(Res.string.home_spending_limit_set),
                style = MaterialTheme.typography.labelLarge
            )
        },
        text = {
            BasicTextField(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .wrapContentHeight()
                    .border(
                        0.5.dp,
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(horizontal = 8.dp),
                value = limitText,
                onValueChange = {
                    limitText = it
                },
                maxLines = 1,
                singleLine = true,
                decorationBox = {
                    Box(
                        modifier = Modifier.padding(8.dp),
                        ){
                        if (limitText.isEmpty()){
                            Text(
                                text = stringResource(Res.string.home_spending_limit_set_hint),
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontSize = 14.sp
                                )
                            )
                        }
                        it()
                    }
                },
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = MaterialTheme.colorScheme.onBackground
                ),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword
                ),
                cursorBrush = SolidColor(
                    value = MaterialTheme.colorScheme.onBackground
                )
            )
        }
    )

}