package feature.add.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.core.presentation.Texts
import feature.core.ui.dark_CorrectColorContainer
import feature.core.ui.dark_ErrorColorContainer
import feature.core.ui.light_CorrectColorContainer
import feature.core.ui.light_ErrorColorContainer
import feature.core.ui.light_onCorrectColor
import feature.core.ui.light_onErrorColor
import feature.core.ui.md_theme_dark_outline
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CostTypeSelect(
    modifier: Modifier = Modifier,
    isIncome: Boolean = false,
    onTypeChange: (Boolean) -> Unit = {},
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    onCloseClick: () -> Unit
){
    Row(
        modifier = modifier.fillMaxWidth()
    ) {
        IconButton(
            onClick = onCloseClick
        ){
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = null
            )
        }

        Box(
            modifier = Modifier
                .weight(1f)
                .clickable { onTypeChange(false) }
                .background(
                    if (!isIncome) {
                        light_ErrorColorContainer
                    } else {
                        light_onErrorColor
                    },
//                    if (!isIncome) {
//                        if (isSystemInDarkTheme()) dark_CorrectColorContainer else light_CorrectColorContainer
//                    } else {
//                        if (isSystemInDarkTheme()) md_theme_dark_outline else light_onCorrectColor
//
//                    },
                    shape = shape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = shape
                )
            ,
            contentAlignment = Alignment.Center){
            Texts.BodyMedium(
                modifier = Modifier.padding(16.dp),
                text = stringResource(Res.string.expense)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .weight(1f)
                .clickable {
                    onTypeChange(true)
                }
                .background(
                    if (isIncome) {
                        light_CorrectColorContainer
                    } else {
                        light_onCorrectColor
                    },
//                    if (isIncome) {
//                        if (isSystemInDarkTheme()) dark_ErrorColorContainer else light_ErrorColorContainer
//                    } else {
//                        if (isSystemInDarkTheme()) md_theme_dark_outline else light_onErrorColor
//                    },
                    shape = shape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = shape
                )
            ,
            contentAlignment = Alignment.Center
        ){
            Texts.BodyMedium(
                modifier = Modifier.padding(16.dp),
                text =  stringResource(Res.string.income),
                style = MaterialTheme.typography.bodyMedium,
            )
        }

    }
}