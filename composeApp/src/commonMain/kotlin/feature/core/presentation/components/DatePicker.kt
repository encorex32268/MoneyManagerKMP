@file:OptIn(ExperimentalResourceApi::class)

package feature.core.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import feature.core.domain.util.zeroStringDisplay
import feature.core.presentation.Texts
import formatString
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    year: Int,
    month: Int,
    onDateChange: (Int,Int) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = {
                when(month - 1){
                    0 -> {
                        onDateChange(
                            year - 1,
                            12
                        )
                    }
                    else -> {
                        onDateChange(
                            year,
                            month - 1
                        )
                    }
                }
            }
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = null
            )
        }

        Texts.TitleSmall(
            modifier = Modifier.weight(1f),
            text = "${year}/${zeroStringDisplay(month)}",
            textAlign = TextAlign.Center
        )
        IconButton(
            onClick = {
                when(month + 1){
                    13 -> {
                        onDateChange(
                            year + 1,
                            1
                        )
                    }
                    else -> {
                        onDateChange(
                            year,
                            month + 1
                        )
                    }
                }
            }
        ){
            Icon(
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null
            )
        }
    }



}
