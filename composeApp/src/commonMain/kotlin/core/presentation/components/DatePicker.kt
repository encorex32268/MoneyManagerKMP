@file:OptIn(ExperimentalResourceApi::class)

package core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import core.domain.util.zeroStringDisplay
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun DatePicker(
    modifier: Modifier = Modifier,
    year: Int,
    month: Int,
    onDateChange: (Int,Int) -> Unit
) {
    val dateString = remember(year,month){
        "${year}/${zeroStringDisplay(month)}"
    }
    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
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


        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Center,
            text = dateString,
            style = MaterialTheme.typography.bodyLarge

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
