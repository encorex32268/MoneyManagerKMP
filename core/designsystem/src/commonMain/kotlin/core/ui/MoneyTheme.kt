package core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class MoneyColors(
    val bg: Color,
    val surface: Color,
    val surfaceAlt: Color,
    val ink: Color,
    val muted: Color,
    val line: Color,
    val cardBorder: Color,
    val accent: Color,
    val accentFg: Color,
    val budgetSafe: Color,
    val budgetWarn: Color,
    val budgetDanger: Color,
    val numpadSheet: Color,
    val numpadKey: Color,
    val numpadKeyFg: Color,
    val numpadDelete: Color,
    val numpadDeleteFg: Color,
    val numpadDate: Color,
    val numpadDateFg: Color,
    val numpadDone: Color,
    val numpadDoneFg: Color,
)

val LightMoneyColors = MoneyColors(
    bg = MoneyTokens.LightBg,
    surface = MoneyTokens.LightSurface,
    surfaceAlt = MoneyTokens.LightSurfaceAlt,
    ink = MoneyTokens.LightInk,
    muted = MoneyTokens.LightMuted,
    line = MoneyTokens.LightLine,
    cardBorder = MoneyTokens.LightCardBorder,
    accent = MoneyTokens.LightInk,
    accentFg = MoneyTokens.LightSurface,
    budgetSafe = MoneyTokens.BudgetSafe,
    budgetWarn = MoneyTokens.BudgetWarn,
    budgetDanger = MoneyTokens.BudgetDanger,
    numpadSheet = MoneyTokens.NumpadSheetLight,
    numpadKey = MoneyTokens.NumpadKeyLight,
    numpadKeyFg = MoneyTokens.NumpadKeyFgLight,
    numpadDelete = MoneyTokens.NumpadDelLight,
    numpadDeleteFg = MoneyTokens.NumpadDelFgLight,
    numpadDate = MoneyTokens.NumpadDateLight,
    numpadDateFg = MoneyTokens.NumpadDateFgLight,
    numpadDone = MoneyTokens.NumpadDoneLight,
    numpadDoneFg = MoneyTokens.NumpadDoneFgLight,
)

val DarkMoneyColors = MoneyColors(
    bg = MoneyTokens.DarkBg,
    surface = MoneyTokens.DarkSurface,
    surfaceAlt = MoneyTokens.DarkSurfaceAlt,
    ink = MoneyTokens.DarkInk,
    muted = MoneyTokens.DarkMuted,
    line = MoneyTokens.DarkLine,
    cardBorder = MoneyTokens.DarkCardBorder,
    accent = MoneyTokens.DarkInk,
    accentFg = MoneyTokens.DarkBg,
    budgetSafe = MoneyTokens.BudgetSafe,
    budgetWarn = MoneyTokens.BudgetWarn,
    budgetDanger = MoneyTokens.BudgetDanger,
    numpadSheet = MoneyTokens.NumpadSheetDark,
    numpadKey = MoneyTokens.NumpadKeyDark,
    numpadKeyFg = MoneyTokens.NumpadKeyFgDark,
    numpadDelete = MoneyTokens.NumpadDelDark,
    numpadDeleteFg = MoneyTokens.NumpadDelFgDark,
    numpadDate = MoneyTokens.NumpadDateDark,
    numpadDateFg = MoneyTokens.NumpadDateFgDark,
    numpadDone = MoneyTokens.NumpadDoneDark,
    numpadDoneFg = MoneyTokens.NumpadDoneFgDark,
)

val LocalMoneyColors = staticCompositionLocalOf { LightMoneyColors }

object MoneyTheme {
    val colors: MoneyColors
        @Composable
        @ReadOnlyComposable
        get() = LocalMoneyColors.current
}
