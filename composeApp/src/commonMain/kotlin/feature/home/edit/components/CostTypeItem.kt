@file:OptIn(ExperimentalResourceApi::class)

package feature.home.edit.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import feature.core.presentation.Texts
import feature.core.ui.light_CorrectColorContainer
import feature.core.ui.light_ErrorColorContainer
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.expense
import moneymanagerkmp.composeapp.generated.resources.income
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.stringResource

@Composable
fun CostTypeItem(
    modifier: Modifier = Modifier,
    isIncome: Boolean,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp)
) {
    Box(
        modifier = modifier
            .background(
                color = if (isIncome) {
                    light_CorrectColorContainer
                } else {
                    light_ErrorColorContainer
                },
                shape = shape
            )
        ,
        contentAlignment = Alignment.Center
    ){
        Texts.BodyMedium(
            modifier = Modifier.padding(4.dp),
            text =  stringResource(if (isIncome) Res.string.income else Res.string.expense),
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}
