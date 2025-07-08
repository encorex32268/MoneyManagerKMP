@file:OptIn(ExperimentalResourceApi::class)

package core.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import core.domain.util.zeroStringDisplay
import core.ui.textColor
import org.jetbrains.compose.resources.ExperimentalResourceApi

@Composable
fun DatePicker(
    year: Int,
    month: Int,
    onDateChange: (Int,Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val dateString = remember(year,month){
        "${year}-${zeroStringDisplay(month)}"
    }
    Row(
        modifier = modifier.fillMaxWidth().padding(horizontal = 8.dp),
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
                modifier = Modifier.size(32.dp),
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowLeft,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.textColor()
            )
        }


        Text(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            textAlign = TextAlign.Center,
            text = dateString,
            style = MaterialTheme.typography.titleLarge

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
                modifier = Modifier.size(32.dp),
                imageVector = Icons.AutoMirrored.Default.KeyboardArrowRight,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.textColor()
            )
        }
    }



}
