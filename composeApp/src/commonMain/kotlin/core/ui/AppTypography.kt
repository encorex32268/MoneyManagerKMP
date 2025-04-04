package core.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.notosanslao_bold
import moneymanagerkmp.composeapp.generated.resources.notosanslao_extrabold
import moneymanagerkmp.composeapp.generated.resources.notosanslao_medium
import moneymanagerkmp.composeapp.generated.resources.notosanslao_regular
import moneymanagerkmp.composeapp.generated.resources.notosanslao_semibold
import org.jetbrains.compose.resources.Font

@Composable
fun getTypography() : Typography {
    val notosanslao = FontFamily(
        fonts =listOf(
            Font(
                resource = Res.font.notosanslao_regular,
                weight = FontWeight.Normal,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.notosanslao_medium,
                weight = FontWeight.Medium,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.notosanslao_semibold,
                weight = FontWeight.SemiBold,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.notosanslao_bold,
                weight = FontWeight.Bold,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.notosanslao_extrabold,
                weight = FontWeight.ExtraBold,
                style = FontStyle.Normal
            )
        )
    )

    return Typography(
        bodyLarge = TextStyle(
            fontFamily = notosanslao,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp
        ),
        bodyMedium = TextStyle(
            fontFamily = notosanslao,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
        ),
        bodySmall = TextStyle(
            fontFamily = notosanslao,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light
        ),

        labelLarge = TextStyle(
            fontFamily = notosanslao,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp
        ),
        labelMedium = TextStyle(
            fontFamily = notosanslao,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        ),
        labelSmall = TextStyle(
            fontFamily = notosanslao,
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin
        ),


        titleSmall = TextStyle(
            fontFamily = notosanslao,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )


    )
}
