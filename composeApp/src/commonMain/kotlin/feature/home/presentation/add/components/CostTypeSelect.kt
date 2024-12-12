package feature.home.presentation.add.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.rounded.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import feature.core.presentation.Texts
import feature.core.ui.light_CorrectColorContainer
import feature.core.ui.light_ErrorColorContainer
import feature.core.ui.light_onCorrectColor
import feature.core.ui.light_onErrorColor
import feature.core.presentation.noRippleClick
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.baseline_edit_note_24
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.resources.vectorResource

@OptIn(ExperimentalResourceApi::class)
@Composable
fun CostTypeSelect(
    modifier: Modifier = Modifier,
    isIncome: Boolean = false,
    onTypeChange: (Boolean) -> Unit = {},
    shape: RoundedCornerShape = RoundedCornerShape(20.dp)
){
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .noRippleClick { onTypeChange(false) }
                .background(
                    if (!isIncome) {
                        light_ErrorColorContainer
                    } else {
                        light_onErrorColor
                    },
                    shape = shape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = shape
                )
                .padding(horizontal = 32.dp , vertical = 8.dp)
            ,
            contentAlignment = Alignment.Center){

            Text(
                text = stringResource(Res.string.expense),
                style = MaterialTheme.typography.bodyMedium
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Box(
            modifier = Modifier
                .noRippleClick {
                    onTypeChange(true)
                }
                .background(
                    if (isIncome) {
                        light_CorrectColorContainer
                    } else {
                        light_onCorrectColor
                    },
                    shape = shape
                )
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.onSurface,
                    shape = shape
                )
                .padding(horizontal = 32.dp, vertical = 8.dp)
            ,
            contentAlignment = Alignment.Center
        ){
            Text(
                text = stringResource(Res.string.income),
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}