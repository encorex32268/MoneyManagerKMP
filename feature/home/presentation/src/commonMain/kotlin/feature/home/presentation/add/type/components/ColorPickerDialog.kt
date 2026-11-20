package feature.home.presentation.add.type.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import core.presentation.noRippleClick
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.category_add_description
import org.jetbrains.compose.resources.stringResource

@Composable
fun ColorPickerDialog(
    onDismissRequest: () -> Unit = {},
    onDone: (Int , String) -> Unit = { _ , _ -> },
    currentColor: Int = Color.Black.toArgb(),
    name: String = ""
) {

    val controller = rememberColorPickerController()
    var colorArgb by remember {
        mutableIntStateOf(currentColor)
    }
    var text by remember {
        mutableStateOf("")
    }
    LaunchedEffect(Unit){
        text = name
    }

    Dialog(
        onDismissRequest = onDismissRequest,
        content = {
            val keyboard = LocalSoftwareKeyboardController.current
            val focusManager = LocalFocusManager.current
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .noRippleClick {
                        keyboard?.hide()
                        focusManager.clearFocus(true)
                    }
                    .background(
                        color = MaterialTheme.colorScheme.background,
                        shape = RoundedCornerShape(24.dp)
                    )
                ,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ){
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .border(
                                width = 1.dp,
                                color = Color(colorArgb),
                                shape = RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = Color(colorArgb),
                                shape = RoundedCornerShape(8.dp)
                            )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    BasicTextField(
                        modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .height(36.dp)
                            .border(
                                0.5.dp,
                                color = MaterialTheme.colorScheme.onBackground,
                                shape = RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp)
                            .wrapContentHeight(Alignment.CenterVertically),
                        value = text,
                        onValueChange = {
                            text = it
                        },
                        maxLines = 1,
                        singleLine = true,
                        decorationBox = {
                            if (text.isEmpty()){
                                Text(
                                    text = stringResource(Res.string.category_add_description),
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontSize = 12.sp
                                    )
                                )
                            }
                            it()
                        },
                        textStyle = MaterialTheme.typography.bodyMedium.copy(
                            color = MaterialTheme.colorScheme.onBackground
                        ),
                        cursorBrush = SolidColor(
                            value = MaterialTheme.colorScheme.onBackground
                        )
                    )
                    IconButton(
                        onClick = {
                            onDone(colorArgb , text)
                            onDismissRequest()
                        }) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null
                        )
                    }
                }
                Box{
                    HsvColorPicker(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .padding(32.dp),
                        controller = controller,
                        onColorChanged = { colorEnvelope: ColorEnvelope ->
                            val colorPicker = colorEnvelope.color.toArgb()
                            if (colorPicker != -1){
                                colorArgb = colorPicker
                            }
                        }
                    )

                }


            }
        }
    )



}
