package feature.home.presentation.add.type.components

import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import core.presentation.noRippleClick
import core.ui.MoneyTheme
import core.ui.MoneyTokens
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.category_add_description
import moneymanagerkmp.composeapp.generated.resources.cd_save
import moneymanagerkmp.composeapp.generated.resources.dialog_cancel_button
import org.jetbrains.compose.resources.stringResource

@Composable
fun ColorPickerDialog(
    onDismissRequest: () -> Unit = {},
    onDone: (Int, String) -> Unit = { _, _ -> },
    currentColor: Int = MoneyTokens.MacaronPresets.first().toArgb(),
    name: String = "",
) {
    val controller = rememberColorPickerController()
    var colorArgb by remember { mutableIntStateOf(currentColor) }
    var text by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        text = name
    }

    Dialog(onDismissRequest = onDismissRequest) {
        val keyboard = LocalSoftwareKeyboardController.current
        val focusManager = LocalFocusManager.current

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .noRippleClick {
                    keyboard?.hide()
                    focusManager.clearFocus(true)
                },
            shape = RoundedCornerShape(MoneyTokens.ModalRadius),
            color = MoneyTheme.colors.surface,
            shadowElevation = 12.dp,
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .background(color = Color(colorArgb), shape = CircleShape)
                            .border(1.dp, MoneyTheme.colors.line, CircleShape)
                    )
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(44.dp)
                            .background(
                                color = MoneyTheme.colors.bg,
                                shape = RoundedCornerShape(13.dp),
                            )
                            .border(
                                width = 1.dp,
                                color = MoneyTheme.colors.line,
                                shape = RoundedCornerShape(13.dp),
                            )
                            .padding(horizontal = 14.dp),
                        contentAlignment = Alignment.CenterStart,
                    ) {
                        BasicTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = text,
                            onValueChange = { text = it },
                            maxLines = 1,
                            singleLine = true,
                            decorationBox = { innerTextField ->
                                if (text.isEmpty()) {
                                    Text(
                                        text = stringResource(Res.string.category_add_description),
                                        style = MaterialTheme.typography.bodyMedium,
                                        color = MoneyTheme.colors.muted,
                                    )
                                }
                                innerTextField()
                            },
                            textStyle = MaterialTheme.typography.bodyLarge.copy(
                                color = MoneyTheme.colors.ink,
                            ),
                            cursorBrush = SolidColor(MoneyTheme.colors.ink),
                        )
                    }
                    Surface(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(RoundedCornerShape(13.dp))
                            .clickable {
                                onDone(colorArgb, text)
                                onDismissRequest()
                            },
                        shape = RoundedCornerShape(13.dp),
                        color = MoneyTheme.colors.accent,
                        contentColor = MoneyTheme.colors.accentFg,
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                modifier = Modifier.size(20.dp),
                                imageVector = Icons.Default.Check,
                                contentDescription = stringResource(Res.string.cd_save),
                            )
                        }
                    }
                }

                HsvColorPicker(
                    modifier = Modifier
                        .size(180.dp)
                        .padding(8.dp),
                    controller = controller,
                    onColorChanged = { colorEnvelope: ColorEnvelope ->
                        val colorPicker = colorEnvelope.color.toArgb()
                        if (colorPicker != -1) {
                            colorArgb = colorPicker
                        }
                    },
                )

                // 8 Macaron Category Presets
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    MoneyTokens.MacaronPresets.forEach { preset ->
                        val presetArgb = preset.toArgb()
                        val isSelected = presetArgb == colorArgb
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .then(
                                    if (isSelected) {
                                        Modifier.border(2.dp, MoneyTheme.colors.ink, CircleShape)
                                    } else {
                                        Modifier
                                    }
                                )
                                .padding(if (isSelected) 3.dp else 0.dp)
                                .clip(CircleShape)
                                .background(preset)
                                .clickable { colorArgb = presetArgb }
                        )
                    }
                }

                OutlinedButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp),
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, MoneyTheme.colors.line),
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MoneyTheme.colors.ink,
                    ),
                    onClick = onDismissRequest,
                ) {
                    Text(
                        text = stringResource(Res.string.dialog_cancel_button),
                        style = MaterialTheme.typography.labelLarge,
                    )
                }
            }
        }
    }
}
