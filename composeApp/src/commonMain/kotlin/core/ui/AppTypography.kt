package core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.notosanstc_bold
import moneymanagerkmp.composeapp.generated.resources.notosanstc_extrabold
import moneymanagerkmp.composeapp.generated.resources.notosanstc_medium
import moneymanagerkmp.composeapp.generated.resources.notosanstc_regular
import moneymanagerkmp.composeapp.generated.resources.notosanstc_semibold
import org.jetbrains.compose.resources.Font


@Composable
fun getTypography() : Typography {
    val notosanstc = FontFamily(
        fonts =listOf(
            Font(
                resource = Res.font.notosanstc_regular,
                weight = FontWeight.Normal,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.notosanstc_medium,
                weight = FontWeight.Medium,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.notosanstc_semibold,
                weight = FontWeight.SemiBold,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.notosanstc_bold,
                weight = FontWeight.Bold,
                style = FontStyle.Normal
            ),
            Font(
                resource = Res.font.notosanstc_extrabold,
                weight = FontWeight.ExtraBold,
                style = FontStyle.Normal
            )
        )
    )


    val old =  Typography(
        bodyLarge = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.textColor()
        ),
        bodyMedium = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.textColor()
        ),
        bodySmall = TextStyle(
            fontFamily = notosanstc,
            fontSize = 12.sp,
            fontWeight = FontWeight.Light,
            color = MaterialTheme.colorScheme.textColor()
        ),

        labelLarge = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.textColor()
        ),
        labelMedium = TextStyle(
            fontFamily = notosanstc,
            fontSize = 14.sp,
            fontWeight = FontWeight.Normal,
            color = MaterialTheme.colorScheme.textColor()
        ),
        labelSmall = TextStyle(
            fontFamily = notosanstc,
            fontSize = 12.sp,
            fontWeight = FontWeight.Thin,
            color = MaterialTheme.colorScheme.textColor()
        ),
        titleSmall = TextStyle(
            fontFamily = notosanstc,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.textColor()
        )


    )

    val QashTypography = Typography(
        // Display
        displayLarge = TextStyle(
            fontFamily = notosanstc, // 或 appFontFamily
            fontWeight = FontWeight.Light,
            fontSize = 57.sp,
            lineHeight = 64.sp,
            letterSpacing = (-0.25).sp
        ),
        displayMedium = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Light,
            fontSize = 45.sp,
            lineHeight = 52.sp,
            letterSpacing = 0.sp
        ),
        displaySmall = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Normal,
            fontSize = 36.sp,
            lineHeight = 44.sp,
            letterSpacing = 0.sp
        ),

        // Headline
        headlineLarge = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Normal,
            fontSize = 32.sp,
            lineHeight = 40.sp,
            letterSpacing = 0.sp
        ),
        headlineMedium = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Normal,
            fontSize = 28.sp,
            lineHeight = 36.sp,
            letterSpacing = 0.sp
        ),
        headlineSmall = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Normal,
            fontSize = 24.sp,
            lineHeight = 32.sp,
            letterSpacing = 0.sp
        ),

        // Title
        titleLarge = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Medium,
            fontSize = 22.sp,
            lineHeight = 28.sp,
            letterSpacing = 0.sp
        ),
        titleMedium = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Medium, // 通常用於次要標題，字重可略高
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.15.sp
        ),
        titleSmall = TextStyle(
            fontFamily = notosanstc,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),

        // Body (這是您最常用來顯示內文的，例如列表項目描述、數字等)
        bodyLarge = TextStyle( // 主要的內文，例如您的帳目列表中的金額和描述
            fontFamily = notosanstc,
            fontWeight = FontWeight.Normal,
            fontSize = 16.sp,
            lineHeight = 24.sp,
            letterSpacing = 0.5.sp
        ),
        bodyMedium = TextStyle( // 次要內文或更小的描述
            fontFamily = notosanstc,
            fontWeight = FontWeight.Normal,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.25.sp
        ),
        bodySmall = TextStyle( // 最細小的內文，如備註或日期
            fontFamily = notosanstc,
            fontWeight = FontWeight.Normal,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.4.sp
        ),

        // Label (用於 UI 元件的標籤、按鈕文字、小提示等)
        labelLarge = TextStyle( // 按鈕文字、導航文字
            fontFamily = notosanstc,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            letterSpacing = 0.1.sp
        ),
        labelMedium = TextStyle( // 小徽章、日期選擇器、Input 欄位提示文字
            fontFamily = notosanstc,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        ),
        labelSmall = TextStyle( // 最小的輔助信息、錯誤提示
            fontFamily = notosanstc,
            fontWeight = FontWeight.Medium,
            fontSize = 11.sp,
            lineHeight = 16.sp,
            letterSpacing = 0.5.sp
        )
    )

    return QashTypography
}



