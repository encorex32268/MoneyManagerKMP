package core.ui

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import moneymanagerkmp.composeapp.generated.resources.Res
import moneymanagerkmp.composeapp.generated.resources.plus_jakarta_sans_bold
import moneymanagerkmp.composeapp.generated.resources.plus_jakarta_sans_extrabold
import moneymanagerkmp.composeapp.generated.resources.plus_jakarta_sans_medium
import moneymanagerkmp.composeapp.generated.resources.plus_jakarta_sans_regular
import moneymanagerkmp.composeapp.generated.resources.plus_jakarta_sans_semibold
import org.jetbrains.compose.resources.Font

private const val TABULAR_NUMS = "tnum"

@Composable
fun getTypography(): Typography {
    val appFontFamily = FontFamily(
        fonts = listOf(
            Font(
                resource = Res.font.plus_jakarta_sans_regular,
                weight = FontWeight.Normal,
                style = FontStyle.Normal,
            ),
            Font(
                resource = Res.font.plus_jakarta_sans_medium,
                weight = FontWeight.Medium,
                style = FontStyle.Normal,
            ),
            Font(
                resource = Res.font.plus_jakarta_sans_semibold,
                weight = FontWeight.SemiBold,
                style = FontStyle.Normal,
            ),
            Font(
                resource = Res.font.plus_jakarta_sans_bold,
                weight = FontWeight.Bold,
                style = FontStyle.Normal,
            ),
            Font(
                resource = Res.font.plus_jakarta_sans_extrabold,
                weight = FontWeight.ExtraBold,
                style = FontStyle.Normal,
            ),
        )
    )

    return Typography(
        // Hero Total: 33sp ExtraBold (lh 1.15)
        displaySmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 33.sp,
            lineHeight = 38.sp,
            letterSpacing = (-0.8).sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Numpad Amount: 28sp ExtraBold (lh 1.2)
        headlineMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 28.sp,
            lineHeight = 34.sp,
            letterSpacing = (-0.6).sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Chart Total: 22sp ExtraBold (lh 1.2)
        headlineSmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 22.sp,
            lineHeight = 26.sp,
            letterSpacing = (-0.4).sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        headlineLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 32.sp,
            lineHeight = 38.sp,
            letterSpacing = (-0.6).sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        displayLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 44.sp,
            lineHeight = 52.sp,
            letterSpacing = (-0.8).sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        displayMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.ExtraBold,
            fontSize = 36.sp,
            lineHeight = 42.sp,
            letterSpacing = (-0.6).sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Page Title: 18sp Bold (lh 1.3)
        titleLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            lineHeight = 24.sp,
            letterSpacing = (-0.2).sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Pill / Sub Title: 16sp Bold (lh 1.3)
        titleMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            lineHeight = 21.sp,
            letterSpacing = (-0.1).sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Chip / Subtitle: 13sp SemiBold (lh 1.4)
        titleSmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 13.sp,
            lineHeight = 18.sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Primary Item: 15sp SemiBold
        bodyLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 15.sp,
            lineHeight = 22.sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Body / Item: 14sp Medium (lh 1.45)
        bodyMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Meta / Secondary: 12sp Medium (lh 1.35)
        bodySmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Button / Numpad Label: 14sp Bold
        labelLarge = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            lineHeight = 18.sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Date header / Badge: 12sp SemiBold
        labelMedium = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 12.sp,
            lineHeight = 16.sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
        // Bottom Nav Label: 11sp SemiBold
        labelSmall = TextStyle(
            fontFamily = appFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 11.sp,
            lineHeight = 15.sp,
            fontFeatureSettings = TABULAR_NUMS,
        ),
    )
}
