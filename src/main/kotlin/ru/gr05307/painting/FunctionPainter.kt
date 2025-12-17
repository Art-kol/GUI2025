package ru.gr05307.painting

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextMeasurer
import ru.gr05307.model.math.Function
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import kotlin.math.abs

class RobustFunctionPainter(
    override val size: Size,
    private val plain: Plain,
    private val function: Function,
    private val color: Color = Color.Blue,
    private val strokeWidth: Float = 2f,
    private val segments: Int = 1000
) : Painter {

    override fun draw(scope: DrawScope, measurer: TextMeasurer) {
        plain.width = size.width
        plain.height = size.height

        val segmentWidth = size.width / segments

        for (i in 0 until segments) {
            val startX = i * segmentWidth
            val endX = (i + 1) * segmentWidth

            drawSegment(scope, startX, endX)
        }
    }

    private fun drawSegment(
        scope: DrawScope,
        startX: Float,
        endX: Float
    ) {
        val path = Path()
        var drawing = false
        val samples = 100

        val yMinScreen = plain.height

        for (j in 0..samples) {

            val sx = startX + (endX - startX) * j / samples
            val xCart = Converter.xScr2Crt(sx, plain)

            val sy = try {
                var yCart = function(xCart)

                if (!yCart.isFinite()) continue

                if (abs(yCart) > 1e10)
                    yCart = if (yCart > 0) 1e10 else -1e10

                Converter.yCrt2Scr(yCart, plain)
            } catch (_: Exception) {
                continue
            }

            if (sy > yMinScreen) {
                drawing = false
                continue
            }

            if (!drawing) {
                path.moveTo(sx, sy)
                drawing = true
            } else {
                path.lineTo(sx, sy)
            }
        }

        if (!path.isEmpty) {
            scope.drawPath(path, color = color, style = Stroke(width = strokeWidth))
        }
    }
}


/* package ru.gr05307.painting

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.text.TextMeasurer
import ru.gr05307.ui.convertation.Converter
import ru.gr05307.ui.convertation.Plain
import kotlin.math.abs

class RobustFunctionPainter(
    override val size: Size,
    private val plain: Plain,
    private val f: (Double) -> Double,
    private val color: Color = Color.Blue,
    private val strokeWidth: Float = 2f,
    private val segments: Int = 50
) : Painter {

    override fun draw(scope: DrawScope, measurer: TextMeasurer) {
        plain.width = size.width
        plain.height = size.height

        val segmentWidth = size.width / segments

        for (i in 0 until segments) {
            val startX = i * segmentWidth
            val endX = (i + 1) * segmentWidth

            drawSegment(scope, startX, endX)
        }
    }

    private fun drawSegment(scope: DrawScope, startX: Float, endX: Float) {
        val path = Path()
        var firstPoint = true

        val points = mutableListOf<Offset>()
        val samples = 20

        for (j in 0..samples) {
            val sx = startX + (endX - startX) * j / samples
            val xCart = Converter.xScr2Crt(sx, plain)

            try {
                var yCart = f(xCart)
                if (yCart.isFinite()) {
                    if (abs(yCart) > 1e10) {
                        yCart = if (yCart > 0) 1e10 else -1e10
                    }

                    val sy = Converter.yCrt2Scr(yCart, plain)

                    if (sy >= -size.height * 2 && sy <= size.height * 2) {
                        points.add(Offset(sx, sy))
                    }
                }
            } catch (_: Exception) {
            }
        }

        if (points.size >= 2) {
            for (point in points) {
                if (firstPoint) {
                    path.moveTo(point.x, point.y)
                    firstPoint = false
                } else {
                    path.lineTo(point.x, point.y)
                }
            }
            scope.drawPath(path = path, color = color, style = Stroke(width = strokeWidth))
            // Построение графика - объяснить
        }
    }
} */