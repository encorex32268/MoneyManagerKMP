package core.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

val calculateRemoveLightContainerColor = Color(0xFFFFD9DD)
val calculateCalendarLightContainerColor = Color(0xFFe8efff)
val calculateDoneLightContainerColor = Color(0xFFe3ffde)

val calculateRemoveDarkContainerColor = Color(0xFF52393c)
val calculateCalendarDarkContainerColor = Color(0xFF37404e)
val calculateDoneDarkContainerColor = Color(0xFF29462a)

val limitsColor_0_50_Color = Color(0xFF81C784)
val limitsColor_50_70_Color = Color(0xFFFFD54F)
val limitsColor_70_90_Color = Color(0xFFFFB74D)
val limitsColor_100_Color = Color(0xFFE57373)


val primaryLight = Color(0xFF5D5F5F)
val onPrimaryLight = Color(0xFFFFFFFF)
val primaryContainerLight = Color(0xFFF6F6F6)
val onPrimaryContainerLight = Color(0xFF525354)
val secondaryLight = Color(0xFF5E5E5E)
val onSecondaryLight = Color(0xFFFFFFFF)
val secondaryContainerLight = Color(0xFFE8E6E6)
val onSecondaryContainerLight = Color(0xFF4A4A4A)
val tertiaryLight = Color(0xFF605E60)
val onTertiaryLight = Color(0xFFFFFFFF)
val tertiaryContainerLight = Color(0xFFFAF5F8)
val onTertiaryContainerLight = Color(0xFF555355)
val errorLight = Color(0xFFBA1A1A)
val onErrorLight = Color(0xFFFFFFFF)
val errorContainerLight = Color(0xFFFFDAD6)
val onErrorContainerLight = Color(0xFF410002)
val backgroundLight = Color(0xFFf6f6f6) // fix old: 0xFFFCF8F8 new: 0xFFe6e6e6

val BgDark = Color(0xFF030303)
val Bg = Color(0xFF0A0A0A)
val BgLight = Color(0xFF171717)
val Text = Color(0xFFF2F2F2)
val TextMuted = Color(0xFFB0B0B0)
val Highlight = Color(0xFF636363)
val Border = Color(0xFF474747)
val BorderMuted = Color(0xFF2E2E2E)
val Primary = Color(0xFF92B2F2)
val Secondary = Color(0xFFCFAB63)
val Danger = Color(0xFFBB928B)
val Warning = Color(0xFFA6A17D)
val Success = Color(0xFF85A894)
val Info = Color(0xFF8DA0BF)

//val LightBgDark = Color(0xFFE6E6E6)
//val LightBg = Color(0xFFF2F2F2)
//val LightBgLight = Color(0xFFFFFFFF)
//val LightText = Color(0xFF0A0A0A)
//val LightTextMuted = Color(0xFF474747)
//val LightHighlight = Color(0xFFFFFFFF) // 考慮用作onColor
//val LightBorder = Color(0xFF808080)
//val LightBorderMuted = Color(0xFF9E9E9E)
//val LightPrimary = Color(0xFF2B457D)
//val LightSecondary = Color(0xFF473400)
//val LightDanger = Color(0xFF7F5953)
//val LightWarning = Color(0xFF6B6543)
//val LightSuccess = Color(0xFF4A6D5A)
//val LightInfo = Color(0xFF526380)

val ColorScheme.textColor
    get() = @Composable{
        if (isSystemInDarkTheme()){
            Color(0xFFf2f2f2)
        }else{
            Color(0xFF0a0a0a)
        }
    }

val ColorScheme.textMutedColor
    get() = @Composable{
        if (isSystemInDarkTheme()){
            Color(0xFFB0B0B0)
        }else{
            Color(0xFF474747)
        }
    }

val ColorScheme.borderColor
    get() = @Composable {
        if (isSystemInDarkTheme()){
            Color(0xFF474747)
        }else{
            Color(0xFF808080)
        }
    }

 val ColorScheme.highlightColor
    get() = @Composable {
        if (isSystemInDarkTheme()){
            Color(0xFF636363)
        }else{
            Color(0xFFFFFFFF)
        }
    }


val onBackgroundLight = Color(0xFF1C1B1B)
val surfaceLight = Color(0xFFFCF8F8)
val onSurfaceLight = Color(0xFF1C1B1B)
val surfaceVariantLight = Color(0xFFE0E3E3)
val onSurfaceVariantLight = Color(0xFF444748)
val outlineLight = Color(0xFF747878)
val outlineVariantLight = Color(0xFFC4C7C8)
val scrimLight = Color(0xFF000000)
val inverseSurfaceLight = Color(0xFF313030)
val inverseOnSurfaceLight = Color(0xFFF4F0EF)
val inversePrimaryLight = Color(0xFFC6C6C7)
val surfaceDimLight = Color(0xFFDDD9D9)
val surfaceBrightLight = Color(0xFFFCF8F8)
val surfaceContainerLowestLight = Color(0xFFFFFFFF)
val surfaceContainerLowLight = Color(0xFFF6F3F2)
val surfaceContainerLight = Color(0xFFF1EDEC)
val surfaceContainerHighLight = Color(0xFFEBE7E7)
val surfaceContainerHighestLight = Color(0xFFE5E2E1)

val primaryLightMediumContrast = Color(0xFF414343)
val onPrimaryLightMediumContrast = Color(0xFFFFFFFF)
val primaryContainerLightMediumContrast = Color(0xFF737575)
val onPrimaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val secondaryLightMediumContrast = Color(0xFF434343)
val onSecondaryLightMediumContrast = Color(0xFFFFFFFF)
val secondaryContainerLightMediumContrast = Color(0xFF757474)
val onSecondaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryLightMediumContrast = Color(0xFF444245)
val onTertiaryLightMediumContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightMediumContrast = Color(0xFF767476)
val onTertiaryContainerLightMediumContrast = Color(0xFFFFFFFF)
val errorLightMediumContrast = Color(0xFF8C0009)
val onErrorLightMediumContrast = Color(0xFFFFFFFF)
val errorContainerLightMediumContrast = Color(0xFFDA342E)
val onErrorContainerLightMediumContrast = Color(0xFFFFFFFF)
val backgroundLightMediumContrast = Color(0xFFFCF8F8)
val onBackgroundLightMediumContrast = Color(0xFF1C1B1B)
val surfaceLightMediumContrast = Color(0xFFFCF8F8)
val onSurfaceLightMediumContrast = Color(0xFF1C1B1B)
val surfaceVariantLightMediumContrast = Color(0xFFE0E3E3)
val onSurfaceVariantLightMediumContrast = Color(0xFF404344)
val outlineLightMediumContrast = Color(0xFF5C6060)
val outlineVariantLightMediumContrast = Color(0xFF787B7C)
val scrimLightMediumContrast = Color(0xFF000000)
val inverseSurfaceLightMediumContrast = Color(0xFF313030)
val inverseOnSurfaceLightMediumContrast = Color(0xFFF4F0EF)
val inversePrimaryLightMediumContrast = Color(0xFFC6C6C7)
val surfaceDimLightMediumContrast = Color(0xFFDDD9D9)
val surfaceBrightLightMediumContrast = Color(0xFFFCF8F8)
val surfaceContainerLowestLightMediumContrast = Color(0xFFFFFFFF)
val surfaceContainerLowLightMediumContrast = Color(0xFFF6F3F2)
val surfaceContainerLightMediumContrast = Color(0xFFF1EDEC)
val surfaceContainerHighLightMediumContrast = Color(0xFFEBE7E7)
val surfaceContainerHighestLightMediumContrast = Color(0xFFE5E2E1)

val primaryLightHighContrast = Color(0xFF202323)
val onPrimaryLightHighContrast = Color(0xFFFFFFFF)
val primaryContainerLightHighContrast = Color(0xFF414343)
val onPrimaryContainerLightHighContrast = Color(0xFFFFFFFF)
val secondaryLightHighContrast = Color(0xFF222222)
val onSecondaryLightHighContrast = Color(0xFFFFFFFF)
val secondaryContainerLightHighContrast = Color(0xFF434343)
val onSecondaryContainerLightHighContrast = Color(0xFFFFFFFF)
val tertiaryLightHighContrast = Color(0xFF232224)
val onTertiaryLightHighContrast = Color(0xFFFFFFFF)
val tertiaryContainerLightHighContrast = Color(0xFF444245)
val onTertiaryContainerLightHighContrast = Color(0xFFFFFFFF)
val errorLightHighContrast = Color(0xFF4E0002)
val onErrorLightHighContrast = Color(0xFFFFFFFF)
val errorContainerLightHighContrast = Color(0xFF8C0009)
val onErrorContainerLightHighContrast = Color(0xFFFFFFFF)
val backgroundLightHighContrast = Color(0xFFFCF8F8)
val onBackgroundLightHighContrast = Color(0xFF1C1B1B)
val surfaceLightHighContrast = Color(0xFFFCF8F8)
val onSurfaceLightHighContrast = Color(0xFF000000)
val surfaceVariantLightHighContrast = Color(0xFFE0E3E3)
val onSurfaceVariantLightHighContrast = Color(0xFF212525)
val outlineLightHighContrast = Color(0xFF404344)
val outlineVariantLightHighContrast = Color(0xFF404344)
val scrimLightHighContrast = Color(0xFF000000)
val inverseSurfaceLightHighContrast = Color(0xFF313030)
val inverseOnSurfaceLightHighContrast = Color(0xFFFFFFFF)
val inversePrimaryLightHighContrast = Color(0xFFECECEC)
val surfaceDimLightHighContrast = Color(0xFFDDD9D9)
val surfaceBrightLightHighContrast = Color(0xFFFCF8F8)
val surfaceContainerLowestLightHighContrast = Color(0xFFFFFFFF)
val surfaceContainerLowLightHighContrast = Color(0xFFF6F3F2)
val surfaceContainerLightHighContrast = Color(0xFFF1EDEC)
val surfaceContainerHighLightHighContrast = Color(0xFFEBE7E7)
val surfaceContainerHighestLightHighContrast = Color(0xFFE5E2E1)

val primaryDark = Color(0xFFFFFFFF)
val onPrimaryDark = Color(0xFF2F3131)
val primaryContainerDark = Color(0xFFD4D4D4)
val onPrimaryContainerDark = Color(0xFF3E4040)
val secondaryDark = Color(0xFFC8C6C6)
val onSecondaryDark = Color(0xFF303030)
val secondaryContainerDark = Color(0xFF3F3F3F)
val onSecondaryContainerDark = Color(0xFFD5D4D3)
val tertiaryDark = Color(0xFFFFFFFF)
val onTertiaryDark = Color(0xFF313032)
val tertiaryContainerDark = Color(0xFFD8D3D6)
val onTertiaryContainerDark = Color(0xFF403F41)
val errorDark = Color(0xFFFFB4AB)
val onErrorDark = Color(0xFF690005)
val errorContainerDark = Color(0xFF93000A)
val onErrorContainerDark = Color(0xFFFFDAD6)
val backgroundDark = Color(0xFF141313)
val onBackgroundDark = Color(0xFFE5E2E1)
val surfaceDark = Color(0xFF141313)
val onSurfaceDark = Color(0xFFE5E2E1)
val surfaceVariantDark = Color(0xFF444748)
val onSurfaceVariantDark = Color(0xFFC4C7C8)
val outlineDark = Color(0xFF8E9192)
val outlineVariantDark = Color(0xFF444748)
val scrimDark = Color(0xFF000000)
val inverseSurfaceDark = Color(0xFFE5E2E1)
val inverseOnSurfaceDark = Color(0xFF313030)
val inversePrimaryDark = Color(0xFF5D5F5F)
val surfaceDimDark = Color(0xFF141313)
val surfaceBrightDark = Color(0xFF3A3939)
val surfaceContainerLowestDark = Color(0xFF0E0E0E)
val surfaceContainerLowDark = Color(0xFF1C1B1B)
val surfaceContainerDark = Color(0xFF201F1F)
val surfaceContainerHighDark = Color(0xFF2A2A2A)
val surfaceContainerHighestDark = Color(0xFF353434)

val primaryDarkMediumContrast = Color(0xFFFFFFFF)
val onPrimaryDarkMediumContrast = Color(0xFF2F3131)
val primaryContainerDarkMediumContrast = Color(0xFFD4D4D4)
val onPrimaryContainerDarkMediumContrast = Color(0xFF1E2020)
val secondaryDarkMediumContrast = Color(0xFFCCCACA)
val onSecondaryDarkMediumContrast = Color(0xFF161616)
val secondaryContainerDarkMediumContrast = Color(0xFF919090)
val onSecondaryContainerDarkMediumContrast = Color(0xFF000000)
val tertiaryDarkMediumContrast = Color(0xFFFFFFFF)
val onTertiaryDarkMediumContrast = Color(0xFF313032)
val tertiaryContainerDarkMediumContrast = Color(0xFFD8D3D6)
val onTertiaryContainerDarkMediumContrast = Color(0xFF201F21)
val errorDarkMediumContrast = Color(0xFFFFBAB1)
val onErrorDarkMediumContrast = Color(0xFF370001)
val errorContainerDarkMediumContrast = Color(0xFFFF5449)
val onErrorContainerDarkMediumContrast = Color(0xFF000000)
val backgroundDarkMediumContrast = Color(0xFF141313)
val onBackgroundDarkMediumContrast = Color(0xFFE5E2E1)
val surfaceDarkMediumContrast = Color(0xFF141313)
val onSurfaceDarkMediumContrast = Color(0xFFFEFAF9)
val surfaceVariantDarkMediumContrast = Color(0xFF444748)
val onSurfaceVariantDarkMediumContrast = Color(0xFFC8CBCC)
val outlineDarkMediumContrast = Color(0xFFA0A3A4)
val outlineVariantDarkMediumContrast = Color(0xFF808484)
val scrimDarkMediumContrast = Color(0xFF000000)
val inverseSurfaceDarkMediumContrast = Color(0xFFE5E2E1)
val inverseOnSurfaceDarkMediumContrast = Color(0xFF2A2A2A)
val inversePrimaryDarkMediumContrast = Color(0xFF464848)
val surfaceDimDarkMediumContrast = Color(0xFF141313)
val surfaceBrightDarkMediumContrast = Color(0xFF3A3939)
val surfaceContainerLowestDarkMediumContrast = Color(0xFF0E0E0E)
val surfaceContainerLowDarkMediumContrast = Color(0xFF1C1B1B)
val surfaceContainerDarkMediumContrast = Color(0xFF201F1F)
val surfaceContainerHighDarkMediumContrast = Color(0xFF2A2A2A)
val surfaceContainerHighestDarkMediumContrast = Color(0xFF353434)

val primaryDarkHighContrast = Color(0xFFFFFFFF)
val onPrimaryDarkHighContrast = Color(0xFF000000)
val primaryContainerDarkHighContrast = Color(0xFFD4D4D4)
val onPrimaryContainerDarkHighContrast = Color(0xFF000000)
val secondaryDarkHighContrast = Color(0xFFFCFAFA)
val onSecondaryDarkHighContrast = Color(0xFF000000)
val secondaryContainerDarkHighContrast = Color(0xFFCCCACA)
val onSecondaryContainerDarkHighContrast = Color(0xFF000000)
val tertiaryDarkHighContrast = Color(0xFFFFFFFF)
val onTertiaryDarkHighContrast = Color(0xFF000000)
val tertiaryContainerDarkHighContrast = Color(0xFFD8D3D6)
val onTertiaryContainerDarkHighContrast = Color(0xFF000000)
val errorDarkHighContrast = Color(0xFFFFF9F9)
val onErrorDarkHighContrast = Color(0xFF000000)
val errorContainerDarkHighContrast = Color(0xFFFFBAB1)
val onErrorContainerDarkHighContrast = Color(0xFF000000)
val backgroundDarkHighContrast = Color(0xFF141313)
val onBackgroundDarkHighContrast = Color(0xFFE5E2E1)
val surfaceDarkHighContrast = Color(0xFF141313)
val onSurfaceDarkHighContrast = Color(0xFFFFFFFF)
val surfaceVariantDarkHighContrast = Color(0xFF444748)
val onSurfaceVariantDarkHighContrast = Color(0xFFF8FBFC)
val outlineDarkHighContrast = Color(0xFFC8CBCC)
val outlineVariantDarkHighContrast = Color(0xFFC8CBCC)
val scrimDarkHighContrast = Color(0xFF000000)
val inverseSurfaceDarkHighContrast = Color(0xFFE5E2E1)
val inverseOnSurfaceDarkHighContrast = Color(0xFF000000)
val inversePrimaryDarkHighContrast = Color(0xFF282A2B)
val surfaceDimDarkHighContrast = Color(0xFF141313)
val surfaceBrightDarkHighContrast = Color(0xFF3A3939)
val surfaceContainerLowestDarkHighContrast = Color(0xFF0E0E0E)
val surfaceContainerLowDarkHighContrast = Color(0xFF1C1B1B)
val surfaceContainerDarkHighContrast = Color(0xFF201F1F)
val surfaceContainerHighDarkHighContrast = Color(0xFF2A2A2A)
val surfaceContainerHighestDarkHighContrast = Color(0xFF353434)



val lightScheme = lightColorScheme(
    primary = primaryLight,
    onPrimary = onPrimaryLight,
    primaryContainer = primaryContainerLight,
    onPrimaryContainer = onPrimaryContainerLight,
    secondary = secondaryLight,
    onSecondary = onSecondaryLight,
    secondaryContainer = secondaryContainerLight,
    onSecondaryContainer = onSecondaryContainerLight,
    tertiary = tertiaryLight,
    onTertiary = onTertiaryLight,
    tertiaryContainer = tertiaryContainerLight,
    onTertiaryContainer = onTertiaryContainerLight,
    error = errorLight,
    onError = onErrorLight,
    errorContainer = errorContainerLight,
    onErrorContainer = onErrorContainerLight,
    background = backgroundLight,
    onBackground = onBackgroundLight,
    surface = surfaceLight,
    onSurface = onSurfaceLight,
    surfaceVariant = surfaceVariantLight,
    onSurfaceVariant = onSurfaceVariantLight,
    outline = outlineLight,
    outlineVariant = outlineVariantLight,
    scrim = scrimLight,
    inverseSurface = inverseSurfaceLight,
    inverseOnSurface = inverseOnSurfaceLight,
    inversePrimary = inversePrimaryLight,
    surfaceDim = surfaceDimLight,
    surfaceBright = surfaceBrightLight,
    surfaceContainerLowest = surfaceContainerLowestLight,
    surfaceContainerLow = surfaceContainerLowLight,
    surfaceContainer = surfaceContainerLight,
    surfaceContainerHigh = surfaceContainerHighLight,
    surfaceContainerHighest = surfaceContainerHighestLight,
)

val darkScheme = darkColorScheme(
    primary = primaryDark,
    onPrimary = onPrimaryDark,
    primaryContainer = primaryContainerDark,
    onPrimaryContainer = onPrimaryContainerDark,
    secondary = secondaryDark,
    onSecondary = onSecondaryDark,
    secondaryContainer = secondaryContainerDark,
    onSecondaryContainer = onSecondaryContainerDark,
    tertiary = tertiaryDark,
    onTertiary = onTertiaryDark,
    tertiaryContainer = tertiaryContainerDark,
    onTertiaryContainer = onTertiaryContainerDark,
    error = errorDark,
    onError = onErrorDark,
    errorContainer = errorContainerDark,
    onErrorContainer = onErrorContainerDark,
    background = backgroundDark,
    onBackground = onBackgroundDark,
    surface = surfaceDark,
    onSurface = onSurfaceDark,
    surfaceVariant = surfaceVariantDark,
    onSurfaceVariant = onSurfaceVariantDark,
    outline = outlineDark,
    outlineVariant = outlineVariantDark,
    scrim = scrimDark,
    inverseSurface = inverseSurfaceDark,
    inverseOnSurface = inverseOnSurfaceDark,
    inversePrimary = inversePrimaryDark,
    surfaceDim = surfaceDimDark,
    surfaceBright = surfaceBrightDark,
    surfaceContainerLowest = surfaceContainerLowestDark,
    surfaceContainerLow = surfaceContainerLowDark,
    surfaceContainer = surfaceContainerDark,
    surfaceContainerHigh = surfaceContainerHighDark,
    surfaceContainerHighest = surfaceContainerHighestDark,
)








