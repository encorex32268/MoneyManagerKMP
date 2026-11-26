package core.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * MoneyManager Design System — D3 · Ring & Clarity (Nordic Soft Clarity)
 * Shared tokens for Android & iOS via Compose Multiplatform.
 */
object MoneyTokens {
    // Light Palette
    val LightBg = Color(0xFFF3F5F8)
    val LightSurface = Color(0xFFFFFFFF)
    val LightSurfaceAlt = Color(0xFFEDF0F5)
    val LightInk = Color(0xFF111827)
    val LightMuted = Color(0xFF6B7280)
    val LightLine = Color(0xFFE5E9F0)
    val LightCardBorder = Color(0x0F0F172A) // rgba(15, 23, 42, 0.06)

    // Dark Palette
    val DarkBg = Color(0xFF111317)
    val DarkSurface = Color(0xFF1A1D24)
    val DarkSurfaceAlt = Color(0xFF242934)
    val DarkInk = Color(0xFFF3F4F6)
    val DarkMuted = Color(0xFF9CA3AF)
    val DarkLine = Color(0xFF282D3A)
    val DarkCardBorder = Color(0x12FFFFFF) // rgba(255, 255, 255, 0.07)

    // Budget Threshold Semantic
    val BudgetSafe = Color(0xFF10B981)   // < warnMid (default 50%)
    val BudgetWarn = Color(0xFFF59E0B)   // warnMid .. warnHigh
    val BudgetDanger = Color(0xFFEF4444) // >= warnHigh (default 70%)

    // Macaron Category Palette (8 Default Presets)
    val CatLife = Color(0xFFE5B8A8)
    val CatFood = Color(0xFFE2D94D)
    val CatHealth = Color(0xFFE8A8A8)
    val CatTraffic = Color(0xFF7ACCD0)
    val CatWear = Color(0xFFB794EC)
    val CatShop = Color(0xFFDF95E3)
    val CatSports = Color(0xFF87C4E8)
    val CatOther = Color(0xFF8BE08B)

    val MacaronPresets = listOf(
        CatLife,
        CatFood,
        CatHealth,
        CatTraffic,
        CatWear,
        CatShop,
        CatSports,
        CatOther,
    )

    // Pastel Functional Numpad — Light
    val NumpadSheetLight = Color(0xFFF7F5F2)
    val NumpadKeyLight = Color(0xFFFFFFFF)
    val NumpadKeyFgLight = Color(0xFF1F2329)
    val NumpadDelLight = Color(0xFFFDDADF)
    val NumpadDelFgLight = Color(0xFF2A1B1E)
    val NumpadDateLight = Color(0xFFE3ECFF)
    val NumpadDateFgLight = Color(0xFF1E293B)
    val NumpadDoneLight = Color(0xFFDCFCE1)
    val NumpadDoneFgLight = Color(0xFF16301D)

    // Pastel Functional Numpad — Dark
    val NumpadSheetDark = Color(0xFF1B1D24)
    val NumpadKeyDark = Color(0xFF262A34)
    val NumpadKeyFgDark = Color(0xFFF3F4F6)
    val NumpadDelDark = Color(0xFF4B272E)
    val NumpadDelFgDark = Color(0xFFFFD6DD)
    val NumpadDateDark = Color(0xFF243452)
    val NumpadDateFgDark = Color(0xFFD8E6FF)
    val NumpadDoneDark = Color(0xFF21422C)
    val NumpadDoneFgDark = Color(0xFFD1FAD8)

    // Geometry, Radius & Touch Targets
    val ScreenHorizontalPadding = 16.dp
    val CardRadius = 20.dp
    val ModalRadius = 26.dp
    val SheetTopRadius = 28.dp
    val KeyRadius = 20.dp
    val ChipRadius = 12.dp
    val CategoryBoxRadius = 16.dp
    val CategoryBoxSize = 52.dp
    val CategoryIconSize = 26.dp
    val NumpadPillIconBox = 38.dp
    val NumpadPillIcon = 24.dp
    val NumpadKeyHeight = 56.dp
    val FabSize = 56.dp
    val FabRadius = 19.dp
    val BottomBarHeight = 68.dp
    val BadgeIconSize = 36.dp
    val CardElevation = 2.dp

    // Default Budget Warning Thresholds
    const val DefaultWarnMidPercent = 50
    const val DefaultWarnHighPercent = 70
}
