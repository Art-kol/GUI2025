package ru.gr05307.painting

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

class FunctionPainter(
    override val size: Size,
    private val plain: Plain,
    private val f: (Double) -> Double,
    private val color: Color = Color.Blue,
    private val strokeWidth: Float = 2f,
    private val sampleStepPx: Float = 1.0f,
    private val drawPoints: Boolean = false,
    private val maxScreenJumpPx: Float = 100f
) : Painter {

    override fun draw(scope: DrawScope, measurer: TextMeasurer) {
        plain.width = size.width
        plain.height = size.height

        val path = Path()
        val w = size.width
        if (w <= 0f) return

        var firstPoint = true
        var prevYScreen: Float? = null

        var sx = 0f
        while (sx <= w) {
            val xCart = Converter.xScr2Crt(sx, plain)

            val yCart = try {
                f(xCart)
            } catch (e: Exception) {
                Double.NaN
            }

            if (!yCart.isFinite()) {
                prevYScreen = null
                firstPoint = true
            } else {
                val sy = Converter.yCrt2Scr(yCart, plain)

                if (firstPoint) {
                    path.moveTo(sx, sy)
                    firstPoint = false
                } else {
                    val jump = prevYScreen?.let { abs(it - sy) } ?: 0f
                    if (jump.isNaN() || jump > maxScreenJumpPx) {
                        path.moveTo(sx, sy)
                    } else {
                        path.lineTo(sx, sy)
                    }
                }

                if (drawPoints) {
                    scope.drawCircle(color, radius = 2f, center = Offset(sx, sy))
                }

                prevYScreen = sy
            }

            sx += sampleStepPx
        }

        scope.drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
    }
}
