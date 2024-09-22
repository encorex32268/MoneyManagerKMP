package feature.home.add.type.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController

@Composable
fun ColorPickerDialog(
    onDone: (Int) -> Unit = {},
    currentColor: Int = Color.Black.toArgb()
) {
    val controller = rememberColorPickerController()
    var colorArgb by remember {
        mutableStateOf(currentColor)
    }

    Dialog(
        onDismissRequest = {
            onDone(colorArgb)
        },
        content = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Color.White,
                        shape = RoundedCornerShape(24.dp)
                    )
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                HsvColorPicker(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                        .padding(10.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        val colorPicker = colorEnvelope.color.toArgb()
                        if (colorPicker != -1){
                            colorArgb = colorPicker
                        }
                    }
                )
                Box(
                    modifier = Modifier
                        .size(52.dp)
                        .border(
                            width = 1.dp ,
                            color = Color(colorArgb),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .background(
                            color = Color(colorArgb),
                            shape = RoundedCornerShape(8.dp)
                        )
                )
                Spacer(Modifier.height(8.dp))
            }
        }
    )



}
