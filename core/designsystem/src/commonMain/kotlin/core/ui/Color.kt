package core.ui

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Numpad & budget semantic aliases (mapped to MoneyTokens for backward compatibility)
val calculateRemoveLightContainerColor = MoneyTokens.NumpadDelLight
val calculateCalendarLightContainerColor = MoneyTokens.NumpadDateLight
val calculateDoneLightContainerColor = MoneyTokens.NumpadDoneLight

val calculateRemoveDarkContainerColor = MoneyTokens.NumpadDelDark
val calculateCalendarDarkContainerColor = MoneyTokens.NumpadDateDark
val calculateDoneDarkContainerColor = MoneyTokens.NumpadDoneDark

val limitsColor_0_50_Color = MoneyTokens.BudgetSafe
val limitsColor_50_70_Color = MoneyTokens.BudgetWarn
val limitsColor_70_90_Color = MoneyTokens.BudgetDanger
val limitsColor_100_Color = MoneyTokens.BudgetDanger

// Legacy ColorScheme extensions delegated to MoneyTheme.colors
val ColorScheme.textColor
    get() = @Composable { MoneyTheme.colors.ink }

val ColorScheme.textMutedColor
    get() = @Composable { MoneyTheme.colors.muted }

val ColorScheme.borderColor
    get() = @Composable { MoneyTheme.colors.line }

val ColorScheme.borderMutedColor
    get() = @Composable { MoneyTheme.colors.cardBorder }

val ColorScheme.bgColor
    get() = @Composable { MoneyTheme.colors.bg }

val ColorScheme.bglightColor
    get() = @Composable { MoneyTheme.colors.surface }

val ColorScheme.highlightColor
    get() = @Composable { MoneyTheme.colors.numpadKey }

val lightScheme = lightColorScheme(
    primary = MoneyTokens.LightInk,
    onPrimary = MoneyTokens.LightSurface,
    primaryContainer = MoneyTokens.LightSurfaceAlt,
    onPrimaryContainer = MoneyTokens.LightInk,
    secondary = MoneyTokens.LightMuted,
    onSecondary = MoneyTokens.LightSurface,
    secondaryContainer = MoneyTokens.LightSurfaceAlt,
    onSecondaryContainer = MoneyTokens.LightInk,
    tertiary = MoneyTokens.BudgetSafe,
    onTertiary = Color.White,
    tertiaryContainer = MoneyTokens.NumpadDoneLight,
    onTertiaryContainer = MoneyTokens.NumpadDoneFgLight,
    error = MoneyTokens.BudgetDanger,
    onError = Color.White,
    errorContainer = MoneyTokens.NumpadDelLight,
    onErrorContainer = MoneyTokens.NumpadDelFgLight,
    background = MoneyTokens.LightBg,
    onBackground = MoneyTokens.LightInk,
    surface = MoneyTokens.LightSurface,
    onSurface = MoneyTokens.LightInk,
    surfaceVariant = MoneyTokens.LightSurfaceAlt,
    onSurfaceVariant = MoneyTokens.LightMuted,
    outline = MoneyTokens.LightMuted,
    outlineVariant = MoneyTokens.LightLine,
    scrim = Color(0x7A0F172A),
    inverseSurface = MoneyTokens.DarkSurface,
    inverseOnSurface = MoneyTokens.DarkInk,
    inversePrimary = MoneyTokens.DarkInk,
    surfaceDim = MoneyTokens.LightSurfaceAlt,
    surfaceBright = MoneyTokens.LightSurface,
    surfaceContainerLowest = MoneyTokens.LightSurface,
    surfaceContainerLow = MoneyTokens.LightBg,
    surfaceContainer = MoneyTokens.LightSurface,
    surfaceContainerHigh = MoneyTokens.LightSurfaceAlt,
    surfaceContainerHighest = MoneyTokens.LightLine,
)

val darkScheme = darkColorScheme(
    primary = MoneyTokens.DarkInk,
    onPrimary = MoneyTokens.DarkBg,
    primaryContainer = MoneyTokens.DarkSurfaceAlt,
    onPrimaryContainer = MoneyTokens.DarkInk,
    secondary = MoneyTokens.DarkMuted,
    onSecondary = MoneyTokens.DarkBg,
    secondaryContainer = MoneyTokens.DarkSurfaceAlt,
    onSecondaryContainer = MoneyTokens.DarkInk,
    tertiary = MoneyTokens.BudgetSafe,
    onTertiary = MoneyTokens.DarkBg,
    tertiaryContainer = MoneyTokens.NumpadDoneDark,
    onTertiaryContainer = MoneyTokens.NumpadDoneFgDark,
    error = MoneyTokens.BudgetDanger,
    onError = Color.White,
    errorContainer = MoneyTokens.NumpadDelDark,
    onErrorContainer = MoneyTokens.NumpadDelFgDark,
    background = MoneyTokens.DarkBg,
    onBackground = MoneyTokens.DarkInk,
    surface = MoneyTokens.DarkSurface,
    onSurface = MoneyTokens.DarkInk,
    surfaceVariant = MoneyTokens.DarkSurfaceAlt,
    onSurfaceVariant = MoneyTokens.DarkMuted,
    outline = MoneyTokens.DarkMuted,
    outlineVariant = MoneyTokens.DarkLine,
    scrim = Color(0x99000000),
    inverseSurface = MoneyTokens.LightSurface,
    inverseOnSurface = MoneyTokens.LightInk,
    inversePrimary = MoneyTokens.LightInk,
    surfaceDim = MoneyTokens.DarkBg,
    surfaceBright = MoneyTokens.DarkSurfaceAlt,
    surfaceContainerLowest = MoneyTokens.DarkSurface,
    surfaceContainerLow = MoneyTokens.DarkBg,
    surfaceContainer = MoneyTokens.DarkSurface,
    surfaceContainerHigh = MoneyTokens.DarkSurfaceAlt,
    surfaceContainerHighest = MoneyTokens.DarkLine,
)
