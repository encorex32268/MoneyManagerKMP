package core.presentation.components

import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * A receipt-style [Shape] with rounded top corners and a symmetrical serrated (sawtooth) bottom edge.
 */
data class ReceiptShape(
    val cornerRadius: Dp = 16.dp,
    val toothWidth: Dp = 10.dp,
    val toothHeight: Dp = 5.dp,
) : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density,
    ): Outline {
        val cornerRadiusPx = with(density) { cornerRadius.toPx() }
        val toothWidthPx = with(density) { toothWidth.toPx() }
        val toothHeightPx = with(density) { toothHeight.toPx() }

        val path = Path()
        if (size.width <= 0f || size.height <= 0f) {
            return Outline.Generic(path)
        }

        if (toothWidthPx <= 0f || toothHeightPx <= 0f || size.height <= toothHeightPx) {
            path.moveTo(0f, 0f)
            path.lineTo(size.width, 0f)
            path.lineTo(size.width, size.height)
            path.lineTo(0f, size.height)
            path.close()
            return Outline.Generic(path)
        }

        val bottomBaseY = size.height - toothHeightPx
        val radius = cornerRadiusPx.coerceIn(0f, minOf(size.width / 2f, bottomBaseY))

        val toothCount = maxOf(1, (size.width / toothWidthPx).roundToInt())
        val step = size.width / toothCount
        val halfStep = step / 2f

        // Top-left rounded corner -> top edge -> top-right rounded corner
        if (radius > 0f) {
            path.moveTo(0f, radius)
            path.arcTo(
                rect = Rect(0f, 0f, radius * 2f, radius * 2f),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )
            path.lineTo(size.width - radius, 0f)
            path.arcTo(
                rect = Rect(size.width - radius * 2f, 0f, size.width, radius * 2f),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false,
            )
        } else {
            path.moveTo(0f, 0f)
            path.lineTo(size.width, 0f)
        }

        // Right edge
        path.lineTo(size.width, bottomBaseY)

        // Bottom serrated edge (valleys at y = bottomBaseY, peaks at y = size.height)
        for (i in (toothCount - 1) downTo 0) {
            val startX = i * step
            path.lineTo(startX + halfStep, size.height)
            path.lineTo(startX, bottomBaseY)
        }

        // Left edge
        path.close()
        return Outline.Generic(path)
    }
}
