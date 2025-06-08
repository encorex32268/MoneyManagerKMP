package core.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.sourcesans3_variablefont_wght
import org.jetbrains.compose.resources.Font


@Composable
fun getTypography() : Typography {
    val sourcesan3 = FontFamily(
        fonts = listOf<Font>(
            Font(
                resource = Res.font.sourcesans3_variablefont_wght,
                style = FontStyle.Normal,
                weight = FontWeight.ExtraLight
            ),
            Font(
                resource = Res.font.sourcesans3_variablefont_wght,
                style = FontStyle.Normal,
                weight = FontWeight.Light
            ),
            Font(
                resource = Res.font.sourcesans3_variablefont_wght,
                style = FontStyle.Normal,
                weight = FontWeight.Normal
            ),
            Font(
                resource = Res.font.sourcesans3_variablefont_wght,
                style = FontStyle.Normal,
                weight = FontWeight.SemiBold
            ),
            Font(
                resource = Res.font.sourcesans3_variablefont_wght,
                style = FontStyle.Normal,
                weight = FontWeight.Bold
            ),
            Font(
                resource = Res.font.sourcesans3_variablefont_wght,
                style = FontStyle.Normal,
                weight = FontWeight.ExtraBold
            )
        )
    )


    return Typography(
        bodyLarge = TextStyle(
            fontFamily = sourcesan3,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = sourcesan3,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = sourcesan3,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        ),

        labelLarge = TextStyle(
            fontFamily = sourcesan3,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = sourcesan3,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        ),
        labelSmall = TextStyle(
            fontFamily = sourcesan3,
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin
        ),


        titleSmall = TextStyle(
            fontFamily = sourcesan3,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )


    )
}
